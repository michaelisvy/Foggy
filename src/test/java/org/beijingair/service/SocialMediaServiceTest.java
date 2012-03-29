package org.beijingair.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.beijingair.model.TwitterRawMessage;
import org.beijingair.model.WordpressBlog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.TwitterException;

@ContextConfiguration(locations = { "/infrastructure-config.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("production")
public class SocialMediaServiceTest {
	
	private static Logger  logger = Logger.getLogger(SocialMediaServiceTest.class);

	@Autowired
	private SocialMediaService socialMediaService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	private TwitterRawMessage findMaxRawMessage() {
		Session session = sessionFactory.getCurrentSession();
		org.hibernate.Query hibQuery= session.createQuery("from TwitterRawMessage where twitterMessageId=(select max(twitterMessageId) from TwitterRawMessage where city=:city)");
		hibQuery.setString("city", "Beijing");
		TwitterRawMessage message = (TwitterRawMessage) hibQuery.uniqueResult();
		logger.info("latest twitterMessageId: " + message.getTwitterMessageId() + "current city : Beijing");
		return message;
	}
	
	@Test @Transactional
	public void stillAvailableSearchIdOK() throws TwitterException {
		
		TwitterRawMessage message = findMaxRawMessage();
		boolean stillAvailable = socialMediaService.stillAvailableTweet(message);
		Assert.assertTrue("Error: valid Id did not return a result: " + message.getTwitterMessageId(), stillAvailable);
		
	}
	
	
	@Test
	public void stillAvailableSearchIdKO() throws TwitterException {
		long expiredId = 123627748244721664L; // corresponds to october 1th
		TwitterRawMessage message = new TwitterRawMessage( new DateTime().minusDays(20),expiredId,"",1,"");
		logger.info("Trying to access twitter API with an expired Id" + expiredId);
		boolean stillAvailable = socialMediaService.stillAvailableTweet(message);
		Assert.assertFalse("Expired Id did not return a result while tweetId is valid", stillAvailable);
	}
	
	
	@Test @Transactional
	public void findTweetsbyQueryAndSinceIdOK() throws TwitterAccessException {
		TwitterRawMessage message = findMaxRawMessage();
		Query query= new Query("from:BeijingAir");
		List<Tweet> tweetList = this.socialMediaService.findTweetsbyQuery(query, message);
		logger.info("number of results found:" +tweetList.size());
		// no Assert, because tweetList.size() could be "0" if tweets have been updated less than one hour ago
	}
	
	/**
	 * Tests behaviour when sinceId does not exist (or is not available anymore)
	 * Expected behaviour: it should still work and should not take the sinceId into account
	 * @throws TwitterException
	 */
	@Test @Transactional
	public void findTweetsWhenSinceIdKO() throws TwitterAccessException {
		Query query= new Query("from:BeijingAir");
		TwitterRawMessage message = new TwitterRawMessage( new DateTime().minusDays(20),9999L,"",1,"");
		
		List<Tweet> tweetList = this.socialMediaService.findTweetsbyQuery(query, message); //funky sinceId, does not exist
		Assert.assertTrue(tweetList.size()>=1);
		logger.info("number of results found:" +tweetList.size());
		logger.info("first tweet found:" +tweetList.iterator().next().getText());
	}
	
	@Test @Transactional
	public void findLatestWordpressBlog() {
		WordpressBlog blog = this.socialMediaService.findLatestWordpressBlog();
		System.out.println(blog.getTitle());
	}

}
