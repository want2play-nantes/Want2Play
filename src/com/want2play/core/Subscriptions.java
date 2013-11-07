package com.want2play.core;

import java.util.HashMap;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Subscriptions {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	User user;
	
	@Persistent(serialized = "true", defaultFetchGroup="true")
	HashMap<Sport, Boolean> enabledSubscriptions;
	
	public Subscriptions(User user) {
		this.user = user;
		
		enabledSubscriptions = new HashMap<>();
		
		// Initialisation des valeurs
		for(Sport s : Sport.values()) {
			enabledSubscriptions.put(s, false);
		}
	}

	public User getUser() { return user; }

	public HashMap<Sport, Boolean> getEnabledSubscriptions() { return enabledSubscriptions; }
	
	public boolean isEnabled(Sport sport) { return enabledSubscriptions.get(sport); }
	public void setEnabled(Sport sport, Boolean enabled) { enabledSubscriptions.put(sport, enabled); }
	
	public Key getKey() { return key; }
}
