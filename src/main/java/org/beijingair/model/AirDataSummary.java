package org.beijingair.model;

public class AirDataSummary {
	private AirDataInfo airDataInfo;
	private WordpressBlog wordpressBlog;
	private Double aqiYesterdayDifference;
	private boolean increased;
	
	
	public AirDataSummary(AirDataInfo airDataInfo, WordpressBlog wordpressBlog, Double aqiYesterdayDifference) {
		super();
		this.airDataInfo = airDataInfo;
		this.wordpressBlog = wordpressBlog;
		this.aqiYesterdayDifference = aqiYesterdayDifference;
		
		if (aqiYesterdayDifference > 0)
		this.increased = true;
	}

	public boolean isIncreased() {
		return increased;
	}

	public AirDataInfo getAirDataInfo() {
		return airDataInfo;
	}
	public WordpressBlog getWordpressBlog() {
		return wordpressBlog;
	}
	public Double getAqiYesterdayDifference() {
		return aqiYesterdayDifference;
	}
	
	

}
