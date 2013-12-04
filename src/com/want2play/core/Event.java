package com.want2play.core;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Event
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private User creator;
	
	@Persistent
	private String place;
	
	@Persistent
	private Date date;
	
	@Persistent
	private Sport sport;

	@Persistent
	private Integer nbParticipantsMax;
	
	public Event(User creator, String place, Date date, Sport sport, Integer nbParticipantsMax) {
		this.creator = creator;
		this.place = place;
		this.date = date;
		this.sport = sport;
		this.nbParticipantsMax = nbParticipantsMax;
	}

	public User getCreator() { return creator; }

	public String getPlace() { return place; }
	public void setPlace(String place) { this.place = place; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }
	
	public Sport getSport() { return sport; }
	public void setSport(Sport sport) { this.sport = sport; }

	public Integer getNbParticipantsMax() { return nbParticipantsMax; }
	public void setNbParticipantsMax(Integer nbParticipantsMax) { this.nbParticipantsMax = nbParticipantsMax; }
	
	public Key getKey() { return this.key; }
}
