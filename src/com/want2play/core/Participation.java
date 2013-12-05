package com.want2play.core;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Participation implements Comparable<Participation>{
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private User user;
	
	@Persistent
	private Event event;
	
	public Participation(User user, Event event) {
		this.user = user;
		this.event = event;
	}

	public Key getKey() { return key; }
	public String getKeyStr() { return KeyFactory.keyToString(key); }
	
	public User getUser() { return user; }
	public Event getEvent() { return event; }

	@Override
	public int compareTo(Participation o) {
		return event.getDate().compareTo(o.getEvent().getDate());
	}
}
