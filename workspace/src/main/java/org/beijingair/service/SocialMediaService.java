package org.beijingair.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.beijingair.model.TwitterRawMessage;
import org.beijingair.model.WordpressBlog;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Service
public class SocialMediaService {
	
	Twitter twitter = new TwitterFactory().getInstance();
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger  logger = Logger.getLogger(SocialMediaService.class);
	
	@Transactional
	public WordpressBlog findLatestWordpressBlog() {
		org.hibernate.Query query = sessionFactory.getCurrentSession().createQuery("from WordpressBlog where post_status='publish' order by post_date desc");
		query.setMaxResults(1);
		return (WordpressBlog) query.uniqueResult();
	}
	
	/**
	 * check if a tweet is still available based on its id. Tweets are only available for the past seven days.
	 * @param searchId
	 * @return true if tweet is still available
	 * @throws TwitterException
	 */
	protected boolean stillAvailableTweet(TwitterRawMessage message) {
		
		DateTime fiveDaysAgo = new DateTime().minusDays(5);
		if (message.getDate().isAfter(fiveDaysAgo)) {
			return true;
			// if the tweet is not older than 5 days, it is safe to consider that it is still available			
		}
		
		Query query = new Query(message.getTwitterMessageId()+""); // converting it into a String
		
		QueryResult result = null;
		
		List<Tweet> tweetList;
		try {
			result = twitter.search(query);
			tweetList = new ArrayList<Tweet>();
			tweetList = result.getTweets();
		} catch (TwitterException te) {
			// TODO Auto-generated catch block
			logger.error("exception occured when searching for tweet " + message.getTwitterMessageId());
			throw new RuntimeException(te);
		}
		
		if (tweetList.size() > 0) {
			logger.debug("search based on tweetId " + message.getTwitterMessageId() + " returned a result: " +tweetList);
			return true;
		}				
		else {
			logger.debug("search based on tweetId " + message.getTwitterMessageId() + " did not return any result");
			return false;			
		}
	}
	
	/**
	 * 
	 * @param query
	 * @param sinceId: will only be added if the tweet is still available (that is, if it is less than 7 days old)
	 * @return
	 * @throws TwitterException
	 */
	public List<Tweet> findTweetsbyQuery(Query query, TwitterRawMessage message) throws TwitterAccessException {
		try {
			QueryResult result = null;
			// at the maximum: 500 messages per day
			query.setRpp(500);
			
			// sinceId only added if the tweet is still available (that is, if it is less than 7 days old)
			if (stillAvailableTweet(message))
				query.setSinceId(message.getTwitterMessageId());

			List<Tweet> tweetList = new ArrayList<Tweet>(); 
			for (int i = 1; i <= 3; i++) {
				query.setPage(i);
				result = twitter.search(query);
				tweetList.addAll(result.getTweets());	
			}
			
			Collections.sort(tweetList);		
			return tweetList;
		} catch (TwitterException e) {
			logger.error("error when processing Twitter query " + query.getQuery());
			throw new TwitterAccessException(e.getMessage(), e);
		}
	}
	

}
