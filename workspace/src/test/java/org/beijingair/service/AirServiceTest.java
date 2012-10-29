package org.beijingair.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.beijingair.model.AirDataInfo;
import org.beijingair.model.AirDataInfoLight;
import org.beijingair.model.TwitterRawMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@ContextConfiguration(locations={"classpath:infrastructure-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("production")
public class AirServiceTest {
	
	private static Logger  logger = Logger.getLogger(AirServiceTest.class);
	@Autowired
	private AirService airService;
	
	@Test @Transactional
	public void findAirDataInfoNowShanghai() {
		AirDataInfo info = this.airService.findLatestAirDataInfo("Shanghai");
		System.out.println(info);
	}
	
	@Test @Transactional
	public void findAirDataInfoNow() {
		AirDataInfo info = this.airService.findLatestAirDataInfo("Beijing");
		System.out.println(info);
	}
	
	@Test @Transactional
	public void findAirDataInfoToday() {
		List<AirDataInfo> list = this.airService.findAirDataInfoToday("Beijing");
		logger.info(list.size());
		Assert.assertTrue(list.size() > 3);
	}
	
	
	
	@Test @Transactional
	public void findMonthlyAv() { 
		List<AirDataInfoLight> list = this.airService.findMonthlyAverageMax2011AirDataInfo("Beijing");
		System.out.println(list);
	}
	
	@Test @Transactional
	public void findAverageAirDataInfo() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.info(stopWatch.shortSummary());
		List<AirDataInfoLight> list = this.airService.findDailyAverageAirDataInfo("Beijing");
		stopWatch.stop();
		logger.info(stopWatch.shortSummary());
		
		DateTime dateTime = null;
		DateTimeComparator comparator = DateTimeComparator.getInstance();
		
		for (AirDataInfoLight airDataInfo : list) {
			if (dateTime != null) {
				int compareResult = comparator.compare(dateTime, airDataInfo.getDate());
				Assert.assertTrue(compareResult < 0);
			}
			Assert.assertTrue(0 < airDataInfo.getAirQualityIndex());
			Assert.assertTrue(airDataInfo.getAirQualityIndex() < 700);
			
			Assert.assertTrue(0 < airDataInfo.getFineParticleIndex());
			Assert.assertTrue(airDataInfo.getFineParticleIndex() < 700);
			logger.info(airDataInfo);
		}
	}	
	
	@Test @Transactional
	public void findYearlyAverageAirDataInfo() {
		List<AirDataInfoLight> list = this.airService.findYearlyAverageAirDataInfo("Beijing");
		
		System.out.println(list);
	}	
	
	@Test @Transactional
	public void findRawMessage() {
		TwitterRawMessage message = this.airService.findRawMessage(1);
		Assert.assertEquals(message.getCity(), "Beijing");
		Assert.assertNotNull(message.getTwitterMessageId());		
	}

	@Test @Transactional
	public void checkDataConsistency() {
		airService.checkDataConsistency();
		// no need to do anything else: if anything doesn't go as expected, an exception is thrown and the test will fail
	}
	
	@Test @Transactional
	public void processLatestDataBeijing() throws Exception {
		airService.processLatestData("Beijing");
	}
	
	@Test @Transactional
	public void processLatestDataGuangzhou() throws Exception {
		airService.processLatestData("Guangzhou");
	}
	
	@Test @Transactional
	public void processLatestDataKO() throws Exception {
		try {
			airService.processLatestData("aaa");
			Assert.fail("exception was expected because of inconsistent city name");
		} catch (Exception e) {
			logger.info("Exception thrown but expected");
		}
		// no need to do anything else: if anything doesn't go as expected, an exception is thrown and the test will fail
	}
	
	@Test @Transactional
	public void updateAllAirDataInfo() throws Exception {
		airService.updateAllAirDataInfo("Beijing");
	}
	
	
	
	

}
