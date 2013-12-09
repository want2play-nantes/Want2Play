package com.want2play.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
	private DateTime date;
	
	@Persistent
	private DateTime hour;
	
	@Persistent
	private Sport sport;

	@Persistent
	private Integer nbParticipantsMax;
	
	@Persistent
	private List<Key> participants;
	
	public Event(User creator, DateTime date, DateTime hour, String place, Sport sport, Integer nbParticipantsMax) {
		this.creator = creator;
		this.place = place;
		this.date = date;
		this.hour = hour;
		this.sport = sport;
		this.nbParticipantsMax = nbParticipantsMax;
		this.participants = new ArrayList<>();
	}

	public User getCreator() { return creator; }

	public String getPlace() { return place; }
	public void setPlace(String place) { this.place = place; }

	public DateTime getDate() { return date; }
	public void setDate(DateTime date) { this.date = date; }
	
	public DateTime getHour() { return hour; }
	public void setHour(DateTime hour) { this.hour = hour; }
	
	public String getHourStr()
	{
		return DateTimeFormat.forPattern("HH:mm").withLocale(Locale.FRANCE).print(hour);
	}
	
	public String getDateStr()
	{
		return DateTimeFormat.forPattern("EEEE dd MMMM yyyy").withLocale(Locale.FRANCE).print(date);
	}
	
	public String getDateFormStr()
	{
		return DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.FRANCE).print(date);
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
