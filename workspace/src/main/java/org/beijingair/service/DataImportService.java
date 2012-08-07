package org.beijingair.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.beijingair.model.AirDataInfo;
import org.beijingair.model.AirDataInfoLight;
import org.beijingair.model.TwitterRawMessage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataImportService {
	
	@Autowired
	private SessionFactory sessionFactory;

	private static Logger  logger = Logger.getLogger(DataImportService.class);
	

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
	 * checks data consistency between TwitterRawMessage and AirDataInfo
	 */
	@Transactional
	public void checkDataConsistency() {
		Session session = sessionFactory.getCurrentSession();
		long processedMessages = (Long) session.createQuery(
				"select count(id) from TwitterRawMessage message where processed=true").uniqueResult();
		long dataInfos = (Long) session.createQuery("select count(id) from AirDataInfo info").uniqueResult();
		logger.info("processedMessages in TwitterRawMessage: " + processedMessages + ", rows in AirDataInfo: "
				+ dataInfos);
		if (dataInfos != processedMessages) {
			String errorMessage = "tables are unconsistent in the database.\n"
					+ " processedMessages in TwitterRawMessage: " + processedMessages + " rows in AirDataInfo: "
					+ dataInfos;
			throw new RuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beijingair.service.IAirService#findRawMessage(int)
	 */
	@Transactional
	public TwitterRawMessage findRawMessage(int id) {
		Session session = sessionFactory.getCurrentSession();
		org.hibernate.Query query = session.createQuery("from TwitterRawMessage message where id=:id");
		query.setInteger("id", id);
		return (TwitterRawMessage) query.uniqueResult();
	}

	/**
	 * Past 24 hours
	 * 
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AirDataInfo> findAirDataInfoOneDay(DateTime date) {
		Session session = sessionFactory.getCurrentSession();
		org.hibernate.Query query = session
				.createQuery("from AirDataInfo info where info.date>= :minDate and info.date<=:maxDate");
		query.setDate("maxDate", date.toDate());
		query.setDate("minDate", date.minusHours(24).toDate());
		return query.list();
	}

	public List<AirDataInfoLight> findMonthlyAverageAirDataInfo(String city) {
		return findAverageAirData(city, "%M");
	}

	public List<AirDataInfoLight> findWeeklyAverageAirDataInfo(String city) {
		return findAverageAirData(city, "%U");
	}

	private List<AirDataInfoLight> findAverageAirData(String city, String period) {
		Session session = sessionFactory.getCurrentSession();

		org.hibernate.SQLQuery query = session
				.createSQLQuery("Select id, date, AVG(airQualityIndex) as airQualityIndex,"
						+ "AVG(fineParticleIndex) as fineParticleIndex, city from AirData where city=:city "
						+ "group by date_format(date, '" + period + "')");

		query.setString("city", city);
		query.addEntity(AirDataInfoLight.class);

		return query.list();
	}

}
