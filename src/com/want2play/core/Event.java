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
	private String sport;
	
	@Persistent
	private Integer nbParticipants;
	
	public Event(User creator, String place, Date date, String sport, Integer nbParticipants) {
		this.creator = creator;
		this.place = place;
		this.date = date;
		this.sport = sport;
		this.nbParticipants = nbParticipants;
	}

	public User getCreator() { return creator; }

	public String getPlace() { return place; }
	public void setPlace(String place) { this.place = place; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }
	
	public String getSport() { return sport; }
	public void setSport(String sport) { this.sport = sport; }

	public Integer getNbParticipants() { return nbParticipants; }
	public void setNbParticipants(Integer nbParticipants) { this.nbParticipants = nbParticipants; }
	
	public Key getKey() { return this.key; }
}
