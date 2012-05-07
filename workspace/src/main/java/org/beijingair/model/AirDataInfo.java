package org.beijingair.model;

import java.util.StringTokenizer;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Table(name = "AirData")
public class AirDataInfo implements Comparable {

	private static Logger logger = Logger.getLogger(AirDataInfo.class);

	public long getId() {
		return id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Float fineParticleIndex;
	private Integer airQualityIndex;
	private String description;
	private String city;

	@Transient
	private String label;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime date;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rawMessageId")
	private TwitterRawMessage twitterRawMessage;

	public AirDataInfo() {
		super();
	}

	/**
	 * 
	 * @param twitterRawMessage
	 *            sample text message: 10-07-2011; 11:00; PM2.5; 14.0; 45; Good
	 *            // Ozone; no
	 * @return AirDataInfo
	 */
	public static AirDataInfo buildAirDataInfo(TwitterRawMessage twitterRawMessage) {
		try {
			TweetState state = twitterRawMessage.getState();
			// message won't be converted. It is marked as processed and no AirDataInfo is returned
			if (state.equals(TweetState.AVERAGE_24) || state.equals(TweetState.LATEST_HOUR) || state.equals(TweetState.NO_DATA)) {
				twitterRawMessage.setProcessed(2);
				return null;
			}
			
			// message considered as normal, we can process		
			String text = twitterRawMessage.getMessage();
			StringTokenizer tokenizer = new StringTokenizer(text, ";");

			// 10-07-2011; 11:00 --> 10-07-2011-11:00
			String dateStr = tokenizer.nextToken().trim();	
			String timeStr;
			
			// does not contain a ";" : 10-07-2011; 11:00
			if (dateStr.contains(" ")) {
				String temp = dateStr;
				StringTokenizer tokenizerDate = new StringTokenizer(temp, " ");
				dateStr = tokenizerDate.nextToken().trim();
				timeStr = tokenizerDate.nextToken().trim();
				System.out.println("aaa");
			}
			// contains a ";" : 10-07-2011; 11:00
			else {
				timeStr = tokenizer.nextToken().trim();
			}

			String date = dateStr + "-" + timeStr;
			// 1/31/2012 -> 01/31/2012
			if ((date.charAt(1) == '/') || (date.charAt(1) == '/')) {
				 date = "0" + date;
			}

			// Adding those additional controls because Guangzhou_air's format
			// has
			// been somehow inconsistent lately
			// in case time is of format "9:00", we don't bother and just return
			// null
			// the first 2 characters have to be between 0 and 24
			if (!timeStr.startsWith("0") && !timeStr.startsWith("1") && !timeStr.startsWith("2")) {
				twitterRawMessage.setProcessed(3); // malformed
				return null;
			}
			DateTimeFormatter formatter = null;
			

			if (date.charAt(2) == '-')
				formatter = DateTimeFormat.forPattern("MM-dd-yyyy-HH:mm");
			else if (date.charAt(2) == '/')
				formatter = DateTimeFormat.forPattern("MM/dd/yyyy-HH:mm");
			else
				throw new RuntimeException("unknown date format: " + twitterRawMessage.getMessage() + " city: " + twitterRawMessage.getCity()
						+ " \n only MM-dd-yyyy and MM/dd/yyyy are supported.");
			DateTime dateTime = formatter.parseDateTime(date);
			String pm25 = tokenizer.nextToken(); // PM2.5;
			if (!pm25.trim().startsWith("PM")) {
				twitterRawMessage.setProcessed(3); // malformed
				return null; // sometimes Guangzhou_Air is malformed. In that
				// case we just want to skip it.
				
			}

			float fineParticleIndex = Float.parseFloat(tokenizer.nextToken().trim());
			int airQualityIndex = Integer.parseInt(tokenizer.nextToken().trim());

			String description = tokenizer.nextToken().trim();

			return new AirDataInfo(fineParticleIndex, airQualityIndex, description, dateTime, twitterRawMessage);
		} catch (RuntimeException e) {
			logger.error("error when parsing message: " + twitterRawMessage.getMessage() + " city: " + twitterRawMessage.getCity());
			throw e;
		}

	}

	public AirDataInfo(Float fineParticleIndex, Integer airQualityIndex, String description, DateTime date, TwitterRawMessage twitterRawMessage) {
		super();
		this.fineParticleIndex = fineParticleIndex;
		this.airQualityIndex = airQualityIndex;
		this.description = description;
		this.date = date;
		this.twitterRawMessage = twitterRawMessage;
		this.city = twitterRawMessage.getCity();
		this.assignLabel();
	}

	public TwitterRawMessage getTwitterRawMessage() {
		return twitterRawMessage;
	}

	public Float getFineParticleIndex() {
		return fineParticleIndex;
	}

	public Integer getAirQualityIndex() {
		return airQualityIndex;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getDate() {
		return date;
	}

	public String getCity() {
		return city;
	}

	public String getLabel() {
		if (label == null || label.equals("")) {
			this.assignLabel();
		}
		return label;
	}
	
	public String getCapitalizedLabel() {
		String label = getLabel();
		return label.toUpperCase();
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "date; " + date + " airQualityIndex: " + airQualityIndex + " fineParticleIndex: " + fineParticleIndex; 
	}

	@Override
	public int compareTo(Object o) {
		AirDataInfo info = (AirDataInfo) o;
		if (this.getDate().isAfter(info.getDate()))
			return -1;
		else
			return 1;
	}

	public void assignLabel() {
		if (this.airQualityIndex <= 50)
			this.label = "Good";
		else if (this.airQualityIndex <= 100)
			this.label = "Moderate";
		else if (this.airQualityIndex <= 150)
			this.label = "Unhealthy for Sensitive groups";
		else if (this.airQualityIndex <= 200)
			this.label = "Unhealthy";
		else if (this.airQualityIndex <= 300)
			this.label = "Very unhealthy";
		else
			this.label = "Hazardous";
	}

}
