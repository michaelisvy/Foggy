package org.beijingair.batch;

import org.apache.log4j.Logger;
import org.beijingair.service.AirService;
import org.beijingair.service.TwitterAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Triggers data update every one minute for Beijing, Shanghai and Guangzhou
 * So every time a tweet is published, foggybeijing should be updated more than one minute later
 * @author misvy
 *
 */
public class BatchLauncher {
	@Autowired
	private AirService airService;
	
	private static Logger logger = Logger.getLogger(BatchLauncher.class);

	
	/**
	 * executes every one minute
	 * @throws Exception
	 */
	@Scheduled(fixedRate=60000)
	public void processLatestDataBeijing() {
		try {
			airService.processLatestData("Beijing");
			logger.info("processLatestData Beijing succeeded");
		} catch (TwitterAccessException e) {
			logger.error("processLatestData Beijing failed");
		}	
	}
	
	/**
	 * executes every one minute
	 * @throws Exception
	 */
	@Scheduled(fixedRate=60000)
	public void processLatestDataGuangzhou() throws Exception {
		try {
			airService.processLatestData("Guangzhou");
			logger.info("processLatestData Guangzhou succeeded");
		} catch (TwitterAccessException e) {
			logger.error("processLatestData Guangzhou failed");
		}	
	}
	
	@Scheduled(fixedRate=60000)
	public void processLatestDataShanghai() throws Exception {
		try {
			airService.processLatestData("Shanghai");
			logger.info("processLatestData Shanghai succeeded");
		} catch (TwitterAccessException e) {
			logger.error("processLatestData Shanghai failed");
		}	
	}

}
