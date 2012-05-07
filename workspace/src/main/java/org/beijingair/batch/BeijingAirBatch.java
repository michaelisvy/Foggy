package org.beijingair.batch;

import org.apache.log4j.Logger;
import org.beijingair.service.AirService;
import org.beijingair.service.TwitterAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class BeijingAirBatch {
	@Autowired
	private AirService airService;
	
	private static Logger logger = Logger.getLogger(BeijingAirBatch.class);

	
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

}
