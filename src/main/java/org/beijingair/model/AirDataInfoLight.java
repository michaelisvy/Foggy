package org.beijingair.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.PolymorphismType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;


@Entity
@org.hibernate.annotations.Entity(polymorphism=PolymorphismType.IMPLICIT)
@Table(name="AirData")
public class AirDataInfoLight implements Comparable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private Float fineParticleIndex;
	private Integer airQualityIndex;
	private String city;
	
	@Transient
	private String label;	
	
	@Basic
	@Type (type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime date;
		
	

	public AirDataInfoLight() {}	
	
	public AirDataInfoLight(Float fineParticleIndex, Integer airQualityIndex, DateTime date,
			TwitterRawMessage twitterRawMessage) {
		super();
		this.fineParticleIndex = fineParticleIndex;
		this.airQualityIndex = airQualityIndex;
		this.date = date;
		this.city = twitterRawMessage.getCity();
		this.assignLabel();
	} 
	
	public long getId() {
		return id;
	}



	public Float getFineParticleIndex() {
		return fineParticleIndex;
	}

	public Integer getAirQualityIndex() {
		return airQualityIndex;
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


	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "date; " + date + " airQualityIndex: " + airQualityIndex + " fineParticleIndex: " + fineParticleIndex; 
	}




	@Override
	public int compareTo(Object o) {
		AirDataInfoLight info = (AirDataInfoLight) o;
		if (this.getDate().isAfter(info.getDate())) return -1;
		else return 1;
	}
	
	public void assignLabel () {
		if (this.airQualityIndex <= 50)
			this.label = "Good";
		else if (this.airQualityIndex <= 100)
			this.label =  "Moderate";
		else if (this.airQualityIndex <= 150)
			this.label =  "Unhealthy for Sensitive groups";
		else if (this.airQualityIndex <= 200)
			this.label =  "Unhealthy";
		else if (this.airQualityIndex <= 300)
			this.label =  "Very unhealthy";
		else
			this.label =  "Hazardous";
	}
	
	
}
