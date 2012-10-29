package org.beijingair.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.beijingair.model.AirDataInfo;
import org.beijingair.model.AirDataInfoLight;
import org.beijingair.model.AirDataSummary;
import org.beijingair.model.TwitterRawMessage;
import org.beijingair.model.WordpressBlog;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Service
public class AirService {

	private static final String SELECT_ALL = "Select id, date, AVG(airQualityIndex) as airQualityIndex," + "AVG(fineParticleIndex) as fineParticleIndex, city ";

	private static final String DATE_FORMAT_DAY = "date_format(date, '%D'),date_format(date, '%U')," + "date_format(date, '%M'),  date_format(date, '%Y')";

	private static final String DATE_FORMAT_WEEK = "date_format(date, '%U')," + "date_format(date, '%M'),  date_format(date, '%Y')";

	private static final String DATE_FORMAT_MONTH = "date_format(date, '%M'),  date_format(date, '%Y')";

	private static final String DATE_FORMAT_YEAR = "date_format(date, '%Y')";

	private static final String SELECT_DAILY_AVERAGE = SELECT_ALL + " from AirData where city=:city group by " + DATE_FORMAT_DAY;

	@Autowired
	private SocialMediaService socialMediaService;

	private static Map<String, String> citySearchTerms;

	static {
		citySearchTerms = new HashMap<String, String>();
		citySearchTerms.put("Beijing", "from:BeijingAir");
		citySearchTerms.put("Guangzhou", "from:Guangzhou_Air");
		citySearchTerms.put("Shanghai", "from:CGShanghaiAir");
	}

	private static Logger logger = Logger.getLogger(AirService.class);

	Twitter twitter = new TwitterFactory().getInstance();
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * processes all tweets for all cities
	 */
	@Transactional
	public void processLatestData() throws TwitterAccessException {
			for (String city : citySearchTerms.keySet()) {
				this.processLatestData(city);
			}
	}

	/**
	 * processes all tweets for one city
	 */
	@Transactional
	public void processLatestData(String city) throws TwitterAccessException {
			this.persistLatestRawMessages(city);
			this.updateAllAirDataInfo(city);
	}

	@Transactional
	public void importData(String filePath) throws IOException {
		File f = new File(filePath);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(f));

		Session session = sessionFactory.getCurrentSession();

		while (true) {
			String line = bufferedReader.readLine();

			if ((line == null) || line.equals("")) {
				break;
			}
			StringTokenizer tokenizer = new StringTokenizer(line, ";");
			String date = tokenizer.nextToken().trim() + "-" + tokenizer.nextToken().trim();
			// 10-07-2011; 11:00 --> 10-07-2011-11:00
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy-HH:mm");
			DateTime dateTime = formatter.parseDateTime(date);
			TwitterRawMessage message = new TwitterRawMessage(dateTime, -1, line, 0, "Beijing");
			session.save(message);
		}

	}

	/**
	 * Processes messages from the latest twitterMessageId (if this one is not
	 * older than 7 days)
	 * 
	 */
	@Transactional
	public void persistLatestRawMessages(String city) throws TwitterAccessException {

		Session session = sessionFactory.getCurrentSession();
		Query hibQuery = session
				.createQuery("from TwitterRawMessage where twitterMessageId= (select max(twitterMessageId) from TwitterRawMessage where city=:city)");
		hibQuery.setString("city", city);
		TwitterRawMessage message = (TwitterRawMessage) hibQuery.uniqueResult();
		
		twitter4j.Query twitterQuery = new twitter4j.Query(citySearchTerms.get(city));
		// usually message not supposed to be null. But it was at least once when we added Shanghai for the first time		
		if (message != null) {
			twitterQuery.setSinceId(message.getTwitterMessageId());
			logger.debug("latest twitterMessageId: " + message.getTwitterMessageId() + "current city :" + city);			
		}
		List<Tweet> tweets = this.socialMediaService.findTweetsbyQuery(twitterQuery, message);
		persistAllRawMessagesFromTweets(tweets, city);
	}

	/**
	 * All RawMessages are persisted (no matters they are valid or not)
	 * The selection will be made when they will be converted into AirDataInfo
	 * 
	 * @param tweetList
	 */
	@Transactional
	public void persistAllRawMessagesFromTweets(List<Tweet> tweetList, String city) {

		String text = "";
		for (Tweet tweet : tweetList) {
			text = tweet.getText();
			DateTime dt = new DateTime(tweet.getCreatedAt());
			TwitterRawMessage message = new TwitterRawMessage(dt, tweet.getId(), text, 0, city);
			// persisting the message using Hibernate
			Session session = sessionFactory.getCurrentSession();
			session.save(message);
		}
	}

	/**
	 * 
	 */
	@Transactional
	public void updateAllAirDataInfo(String city) {
		Session session = sessionFactory.getCurrentSession();
		checkDataConsistency(); // if number of rows is not consistent in the database, an exception is
								// thrown and the AirDataInfo rows won't be created
		Query query = session.createQuery("from TwitterRawMessage message where processed=:processed and city=:city");		
		query.setInteger("processed", 0); // message that haven't been processed yet
		query.setString("city", city);
		List<TwitterRawMessage> twitterRawMessages = query.list();
		List<AirDataInfo> airDataInfoList = new ArrayList<AirDataInfo>();

		for (TwitterRawMessage rawMessage : twitterRawMessages) {

			AirDataInfo airDataInfo = AirDataInfo.buildAirDataInfo(rawMessage);
			// if null: rawMessage.setProcessed already assigned, no need to do
			// anything else
			if (airDataInfo != null) {
				airDataInfoList.add(airDataInfo);
				rawMessage.setProcessed(1);
			}

		}
		for (AirDataInfo airDataInfo : airDataInfoList) {
			logger.info("processing AirDataInfo: " + airDataInfo);
			session.save(airDataInfo);
		}

	}

	/**
	 * checks data consistency between TwitterRawMessage and AirDataInfo
	 */
	@Transactional
	public void checkDataConsistency() {
		Session session = sessionFactory.getCurrentSession();
		long processedMessages = (Long) session.createQuery("select count(id) from TwitterRawMessage message where processed=1").uniqueResult();
		long dataInfos = (Long) session.createQuery("select count(id) from AirDataInfo info").uniqueResult();
		logger.debug("processedMessages in TwitterRawMessage: " + processedMessages + ", rows in AirDataInfo: " + dataInfos);
		if (dataInfos != processedMessages) {
			String errorMessage = "tables are unconsistent in the database.\n" + " processedMessages in TwitterRawMessage: " + processedMessages
					+ " rows in AirDataInfo: " + dataInfos;
			throw new RuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beijingair.service.IAirService#findRawMessage(int)
	 */
	@Transactional(readOnly = true)
	public TwitterRawMessage findRawMessage(int id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from TwitterRawMessage message where id=:id");
		query.setInteger("id", id);
		return (TwitterRawMessage) query.uniqueResult();
	}
	
	@Transactional(readOnly = true)
	public AirDataInfo findLatestAirDataInfo(String city) {
		Session session = sessionFactory.getCurrentSession();

		// always returns the 6 max results
		Query query = session.createQuery("from AirDataInfo info where info.date = (select max(date) from AirDataInfo where city=:city) and city=:city");
		query.setString("city", city);
		return (AirDataInfo) query.uniqueResult();
	}

	@Transactional(readOnly = true)
	public List<AirDataInfo> findAirDataInfoToday(String city) {
		Session session = sessionFactory.getCurrentSession();

		// always returns the 6 max results
		Query query = session.createQuery("from AirDataInfo where city=:city order by date desc");
		query.setMaxResults(7);
		query.setString("city", city);
		List<AirDataInfo> result = query.list();
		Collections.sort(result);
		return result;
	}
	
	@Transactional(readOnly = true)
	public AirDataSummary findTodaySymmary(String city)  {
		AirDataInfo now = this.findLatestAirDataInfo(city);
		WordpressBlog blog = this.socialMediaService.findLatestWordpressBlog();
		Double aqiDifference = this.findAqiDifferenceComparedToTheDayBefore(city, now.getDate(), now.getAirQualityIndex());
		return new AirDataSummary(now, blog, aqiDifference);		
	}
	
	/**
	 * Calculates weather pollution has increased or decreased compared to the day before
	 * Sample results: 	-10 => decreased 10 points
	 * 					10	 => increased 10 points
	 * 					-1	 => no data
	 * Formula being used: 1-oneDayBeforeAqi/currentAqi
	 */
	@Transactional(readOnly = true)
	double findAqiDifferenceComparedToTheDayBefore(String city, DateTime date, int currentAqi) {
		Session session = this.sessionFactory.getCurrentSession();
		String queryStr= "select avg(airQualityIndex) from AirDataInfo where date>=:dateBefore and date<=:dateAfter";
		DateTime oneDayBeforeDate = date.minusDays(1);
		Date dateBefore = oneDayBeforeDate.minusHours(2).toDate();
		Date dateAfter = oneDayBeforeDate.plusHours(2).toDate();
		Query query = session.createQuery(queryStr);
		query.setDate("dateBefore", dateBefore);
		query.setDate("dateAfter", dateAfter);
		Double oneDayBeforeAqi = (Double) query.uniqueResult();
		return currentAqi - oneDayBeforeAqi;
	}
	
	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findDailyAverageAirDataInfo(String city) {
		DateTime startDate = new DateTime().minusDays(10);

		Session session = sessionFactory.getCurrentSession();

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(SELECT_ALL + " from AirData where city=:city");
		sqlQuery.append(" and date>= :date");
		sqlQuery.append(" group by " + DATE_FORMAT_DAY);
		SQLQuery hibernateQuery = session.createSQLQuery(sqlQuery.toString());
		hibernateQuery.setDate("date", startDate.toDate());
		hibernateQuery.setString("city", city);
		hibernateQuery.addEntity(AirDataInfoLight.class);
		List<AirDataInfoLight> result = hibernateQuery.list();
		Collections.sort(result);
		return result; 
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findMonthlyAverageMax2011AirDataInfo(String city) {
		return findAverageMax2011AirDataInfo(city, DATE_FORMAT_MONTH);
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findYearlyAverageAirDataInfo(String city) {
		return findAverageMax2011AirDataInfo(city, DATE_FORMAT_YEAR);
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findAverageMax2011AirDataInfo(String city, String period) {
		Session session = sessionFactory.getCurrentSession();

		StringBuffer sqlQuery = new StringBuffer();
		// subquery query: daily average for 2010 and 2011
		sqlQuery.append(SELECT_ALL);
		sqlQuery.append("from ( " + SELECT_ALL + " from AirData where city=:city and date< :maxDate ");
		sqlQuery.append("group by " + DATE_FORMAT_DAY + ") as AvAirData");
		// parent query: yearly average
		sqlQuery.append(" group by " + period);

		SQLQuery hibernateQuery = session.createSQLQuery(sqlQuery.toString());
		hibernateQuery.setString("city", city);
		hibernateQuery.setDate("maxDate", new DateTime(2012, 1, 1, 0, 0).toDate());
		hibernateQuery.addEntity(AirDataInfoLight.class);
		List<AirDataInfoLight> result = hibernateQuery.list();
		Collections.sort(result);
		return result;
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findMonthlyAverageAirDataInfo(String city) {
		return findAverageAirDataInfo(city, DATE_FORMAT_MONTH);
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findWeeklyAverageAirDataInfo(String city) {
		return findAverageAirDataInfo(city, DATE_FORMAT_WEEK);
	}

	@Transactional(readOnly = true)
	public List<AirDataInfoLight> findAverageAirDataInfo(String city, String groupByClause) {
		Session session = sessionFactory.getCurrentSession();

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(SELECT_ALL);
		sqlQuery.append("from ( " + SELECT_DAILY_AVERAGE + ") as AvAirData");
		sqlQuery.append(" group by " + groupByClause);

		SQLQuery hibernateQuery = session.createSQLQuery(sqlQuery.toString());
		hibernateQuery.setString("city", city);
		hibernateQuery.addEntity(AirDataInfoLight.class);
		List<AirDataInfoLight> result = hibernateQuery.list();
		Collections.sort(result);
		return result;
	}

}
