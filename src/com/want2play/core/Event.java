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
public class Event implements Comparable<Event>
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
	
	private DateTime getFullDate()
	{
		return new DateTime(
			date.year().get(),
			date.monthOfYear().get(),
			date.dayOfMonth().get(),
			hour.hourOfDay().get(),
			hour.minuteOfHour().get()
		);
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

	@Override
	public int compareTo(Event e)
	{
		return getFullDate().compareTo(e.getFullDate());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((eventKey == null) ? 0 : eventKey.hashCode());
		result = prime * result + ((hour == null) ? 0 : hour.hashCode());
		result = prime
				* result
				+ ((nbParticipantsMax == null) ? 0 : nbParticipantsMax
						.hashCode());
		result = prime * result
				+ ((participants == null) ? 0 : participants.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + ((sport == null) ? 0 : sport.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Event other = (Event) obj;
		
		if (creator == null) {
			if (other.creator != null)
				return false;
		}
		else if (!creator.equals(other.creator))
			return false;
		
		if (date == null) {
			if (other.date != null)
				return false;
		}
		else if (!date.equals(other.date))
			return false;
		
		if (eventKey == null) {
			if (other.eventKey != null)
				return false;
		}
		else if (!eventKey.equals(other.eventKey))
			return false;
		
		if (hour == null) {
			if (other.hour != null)
				return false;
		}
		else if (!hour.equals(other.hour))
			return false;
		
		if (nbParticipantsMax == null) {
			if (other.nbParticipantsMax != null)
				return false;
		}
		else if (!nbParticipantsMax.equals(other.nbParticipantsMax))
			return false;
		
		if (participants == null) {
			if (other.participants != null)
				return false;
		}
		else if (!participants.equals(other.participants))
			return false;
		
		if (place == null) {
			if (other.place != null)
				return false;
		}
		else if (!place.equals(other.place))
			return false;
		
		if (sport != other.sport)
			return false;
		
		return true;
	}
}
