package com.want2play.core;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Participant
{
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key participantKey;
	
	@Persistent
	private User user;
	
	@Persistent
	private List<Key> events;
	
	public Participant(User user) {
		this.user = user;
		this.events = new ArrayList<>();
	}

	public Key getKey() { return participantKey; }
	public String getKeyStr() { return KeyFactory.keyToString(participantKey); }
	
	public User getUser() { return user; }
	
	public List<Key> getEvents() { return events; }
	public void setEvents(List<Key> events) { this.events = events; }
	
	public void addEvent(Event e)
	{
		if (!events.contains(e)) {
			events.add(e.getKey());
			e.getParticipants().add(getKey());
		}
	}
	
	public void removeEvent(Event e)
	{
		events.remove(e.getKey());
		e.getParticipants().remove(getKey());
	}
}
