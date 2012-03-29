package org.beijingair.model;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class AirDataInfoTest {
	
	@Test
	public void buildFromRawTextOKDateComma() {
		String entry = "10-07-2011; 11:00; PM2.5; 14.0; 45; Very Good // Ozone; no data";
		TwitterRawMessage rawMessage = new TwitterRawMessage(new DateTime(), 1000, entry, 0, "Beijing");
		AirDataInfo airDataInfo = AirDataInfo.buildAirDataInfo(rawMessage);
		assertEquals(airDataInfo.getFineParticleIndex().longValue(), 14);
		assertEquals(airDataInfo.getAirQualityIndex().longValue(), 45);		
	}
	
	@Test
	public void buildFromRawTextOKDateNoComma() {
		String entry = "03-03-2012 20:00; PM2.5; 98.0; 170; Unhealthy (at 24-hour exposure at this level)";		
		TwitterRawMessage rawMessage = new TwitterRawMessage(new DateTime(), 1000, entry, 0, "Beijing");
		AirDataInfo airDataInfo = AirDataInfo.buildAirDataInfo(rawMessage);
		assertEquals(airDataInfo.getFineParticleIndex().longValue(), 98);
		assertEquals(airDataInfo.getAirQualityIndex().longValue(), 170);		
	}
	
	@Test
	public void buildFromRawText24h() {
		String entry = "10-01-2011; 00:20; Past 24hr: PM2.5 avg; 26.8; 79; Moderate//Ozone 8hr high; 11.4; 9; Good.";
		TwitterRawMessage rawMessage = new TwitterRawMessage(new DateTime(), 1000, entry, 0, "Beijing");
		AirDataInfo airDataInfo = AirDataInfo.buildAirDataInfo(rawMessage);
		assertEquals(airDataInfo, null);
		assertEquals(rawMessage.getState(), TweetState.AVERAGE_24);		
	}
	
	@Test
	public void buildFromRawTextPM25NoData() {
		String entry = "08-23-2011; 16:00; PM2.5; no data // Ozone; 60.4; 51; Moderate 8:15 AM Aug 23rd";
		TwitterRawMessage rawMessage = new TwitterRawMessage(new DateTime(), 1000, entry, 0, "Beijing");
		AirDataInfo airDataInfo = AirDataInfo.buildAirDataInfo(rawMessage);
		assertEquals(airDataInfo, null);
		assertEquals(rawMessage.getState(), TweetState.NO_DATA);		
	}
}
