package com.want2play.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Event
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key eventKey;
	
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
	
	@Persistent
	private List<Key> participants;
	
	public Event(User creator, String place, Date date, Sport sport, Integer nbParticipantsMax) {
		this.creator = creator;
		this.place = place;
		this.date = date;
		this.sport = sport;
		this.nbParticipantsMax = nbParticipantsMax;
		this.participants = new ArrayList<>();
	}

	public User getCreator() { return creator; }

	public String getPlace() { return place; }
	public void setPlace(String place) { this.place = place; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }
	
	public String getHourStr()
	{
		return new SimpleDateFormat("H:mm").format(date);
	}
	
	public String getDateStr()
	{
		return new SimpleDateFormat("EEEE d MMM yyyy").format(date);
	}
	
	public Sport getSport() { return sport; }
	public void setSport(Sport sport) { this.sport = sport; }

	public Integer getNbParticipantsMax() { return nbParticipantsMax; }
	public void setNbParticipantsMax(Integer nbParticipantsMax) { this.nbParticipantsMax = nbParticipantsMax; }
	
	public List<Key> getParticipants() { return participants; }
	public void setParticipants(List<Key> participations) { this.participants = participations; }
	
	public Integer getNbParticipants() {
		return participants.size();
	}
	
	public void addParticipant(Participant p) {
		if (!participants.contains(p.getKey())) {
			participants.add(p.getKey());
			p.getEvents().add(getKey());
		}
	}
	
	public void removeParticipant(Participant p) {
		participants.remove(p.getKey());
		p.getEvents().remove(getKey());
	}
	
	public Key getKey() { return this.eventKey; }
	public String getKeyStr() { return KeyFactory.keyToString(eventKey); }
}
