package org.beijingair.batch;

import org.beijingair.service.AirService;
import org.springframework.context.support.GenericXmlApplicationContext;
/**
 * Only used when we need to trigger an update by hand
 * @author misvy
 *
 */
public class ManualLauncher {
	
	public static void main(String[] args) throws Exception {
		GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext();
		applicationContext.getEnvironment().setActiveProfiles("production");
		applicationContext.load("classpath:/infrastructure-config.xml");
		applicationContext.refresh();
		AirService airService = (AirService) applicationContext.getBean(AirService.class);
		airService.processLatestData();		// does the job for both cities
	}

}
