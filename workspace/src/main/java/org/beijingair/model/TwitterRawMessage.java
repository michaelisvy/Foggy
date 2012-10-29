package org.beijingair.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
@Entity
@Table(name="RawMessage")
public class TwitterRawMessage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	


	private long twitterMessageId;
	
	private String message;
	
	private int processed;	

	@Basic
	@Type (type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime date;

	
	/*@Type(type="org.beijingair.tools.CityUserType")
	private City city;*/
	private String city;
	
	public TwitterRawMessage(DateTime date, long twitterMessageId, String message, int processed, String city) {
		super();
		this.date = date;
		this.twitterMessageId = twitterMessageId;
		this.message = message;
		this.processed = processed;
		this.city = city;
	}
	
	public TweetState getState() { //Latest Hour
		String uncapMsg = this.message.toLowerCase();
		
		if (uncapMsg.contains("past 24hr")) {
			return TweetState.AVERAGE_24;
		} else if (uncapMsg.contains("latest hour")) {
			return TweetState.LATEST_HOUR;
		}
		else if (uncapMsg.endsWith("Ozone; no data") ) {
			// 11-11-2011; 13:00; PM2.5; 96.0; 169; Unhealthy // Ozone; no data
			return TweetState.OK;
		}
		else if (uncapMsg.contains("no data") && !uncapMsg.contains("ozone; no data") ) {
			// 11-11-2011; 13:00; PM2.5; 96.0; 169; Unhealthy // Ozone; no data
			return TweetState.NO_DATA;
		}
		else if (uncapMsg.contains("due to") || uncapMsg.contains("no reading") ) {
			// includes "pm2.5; no data " and "pm2.5; no data;"
			// 08-18-2012 00:00 to 08-18-2012 23:59; PM2.5 24hr avg; No Data city: Beijing
			return TweetState.NO_DATA;
		}
		else return TweetState.OK;
	}
	
	public TwitterRawMessage() {
		super();
	}

	public int getProcessed() {
		return processed;
	}
	public void setProcessed(int processed) {
		this.processed = processed;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}

	public long getTwitterMessageId() {
		return twitterMessageId;
	}
	public void setTwitterMessageId(long twitterMessageId) {
		this.twitterMessageId = twitterMessageId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	

}
