package com.want2play.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.core.Participant;
import com.want2play.core.Sport;
import com.want2play.core.Subscriptions;

public class DatastoreController {

	private DatastoreController() {
	}

	/**
	 * Retourne true si un evenement existe deja dans le Datastore, false sinon.
	 * 
	 * @param event l'evenement
	 * @return true si l'evenement existe, false sinon
	 */
	public static boolean isExistsEvent(Event event)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Event.class, event.getKey()) != null);
		} catch (Exception e) {
			isExists = false;
		} finally {
			pm.close();
		}

		return isExists;
	}

	/**
	 * Enregistre un evenement dans le Datastore.
	 * 
	 * @param event l'evenement a enregistrer
	 * @return true si l'enregistrement s'est termine avec succes, false sinon
	 */
	public static boolean saveEvent(Event event) {
		if (isExistsEvent(event))
			return false;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.makePersistent(event);
			tx.commit();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Supprime un evenement du Datastore.
	 * 
	 * @param event l'evenement a supprimer
	 * @return true si la suppression s'est terminee avec succes, false sinon
	 */
	public static boolean deleteEvent(Event event)
	{
		if (!isExistsEvent(event))
			return false;
		
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		
		for (Participant p : getParticipantsByEvent(event))
		{
			p.removeEvent(event);
			updateParticipant(p);
		}

		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			
			pm.deletePersistent(pm.getObjectById(Event.class, event.getKey()));
			tx.commit();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Met a jour un evenement dans le Datastore.
	 * 
	 * @param updatedEvent l'evenement a mettre a jour
	 * @return true si la mise a jour s'est terminee avec succes, false sinon
	 */
	public static boolean updateEvent(Event updatedEvent)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();

			Event e = pm.getObjectById(Event.class, updatedEvent.getKey());
			
			if (!e.equals(updatedEvent))
			{
				e.setPlace(updatedEvent.getPlace());
				e.setDate(updatedEvent.getDate());
				e.setHour(updatedEvent.getHour());
				e.setNbParticipantsMax(updatedEvent.getNbParticipantsMax());
				e.setParticipants(updatedEvent.getParticipants());
			}
			else {
				success = false;
			}

			tx.commit();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Retourne un evenement avec sa cle.
	 * 
	 * @param key la cle identifiant l'evenement
	 * @return l'evenement s'il est trouve, null sinon
	 */
	public static Event getEventByKey(Key key)
	{
		Event event;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try
		{
			event = pm.getObjectById(Event.class, key);
		}
		catch (Exception ex) {
			event = null;
		}
		finally {
			pm.close();
		}

		return event;
	}

	/**
	 * Retourne la liste de tous les evenements.
	 * 
	 * @return la liste de tous les evenements
	 */
	@SuppressWarnings("unchecked")
	public static List<Event> getAllEvents()
	{
		List<Event> events;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		
		try
		{
			Query query = pm.newQuery(Event.class);
			query.setOrdering("date desc, hour desc");

			events = (List<Event>) query.execute();

		} finally {
			pm.close();
		}

		return events;
	}

	/**
	 * Retourne la liste de tous les evenements ou l'utilisateur n'est ni le createur, ni un participant.
	 * 
	 * @param user l'utilisateur
	 * @return la liste des evenements
	 */
	public static List<Event> getEventsWithoutUser(User user) {
		List<Event> eventsWithoutUser = new ArrayList<>();

		Participant p = getParticipantByUser(user);

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		Extent<Event> extent = null;

		try {
			extent = pm.getExtent(Event.class, false);
			for (Event e : extent)
			{
				// Si l'utilisateur est le createur de l'evenement ou qu'il y
				// participe, l'evenement n'est pas ajoute
				if ((!user.equals(e.getCreator()) && p != null && !p.getEvents().contains(e.getKey()))
						|| (!user.equals(e.getCreator()) && p == null)) {
					eventsWithoutUser.add(e);
				}
			}
		}
		finally {
			extent.closeAll();
		}

		return eventsWithoutUser;
	}

	/**
	 * Retourne la liste de tous les evenements cree par un utilisateur.
	 * 
	 * @param user l'utilisateur
	 * @return la liste des evenements
	 */
	@SuppressWarnings("unchecked")
	public static List<Event> getEventsByUser(User user) {
		List<Event> userEvents = new ArrayList<>();

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Event.class, "this.creator == user");
			query.declareParameters("com.google.appengine.api.users.User user");
			query.setOrdering("date desc, hour desc");

			userEvents = (List<Event>) query.execute(user);
		} finally {
			pm.close();
		}

		return userEvents;
	}

	/**
	 * Retourne true si un utilisateur est deja abonne, false sinon.
	 * 
	 * @param user l'utilisateur
	 * @return true si un utilisateur est deja abonne, false sinon
	 */
	public static boolean isSubscribedUser(User user) {
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isSubscribed = false;

		try {
			Query query = pm.newQuery(Subscriptions.class, "this.user == user");
			query.declareParameters("com.google.appengine.api.users.User user");

			List<?> l = (List<?>) query.execute(user);

			isSubscribed = (l.size() != 0);
		} finally {
			pm.close();
		}

		return isSubscribed;
	}

	/**
	 * Retourne true si un abonnement existe deja dans le Datastore, false sinon.
	 * 
	 * @param subs l'abonnement
	 * @return true si l'abonnement existe, false sinon
	 */
	public static boolean isExistsSubscriptions(Subscriptions subs) {
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Subscriptions.class, subs.getKey()) != null);
		} catch (Exception e) {
			isExists = false;
		} finally {
			pm.close();
		}

		return isExists;
	}

	/**
	 * Enregistre un abonnement dans le Datastore.
	 * 
	 * @param subs l'abonnement a enregistrer
	 * @return true si l'enregistrement s'est termine avec succes, false sinon
	 */
	public static boolean saveSubscription(Subscriptions subs) {
		if (isSubscribedUser(subs.getUser()))
			return false;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.makePersistent(subs);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Supprime un abonnement du Datastore.
	 * 
	 * @param subs l'abonnement a supprimer
	 * @return true si la suppression s'est terminee avec succes, false sinon
	 */
	public static boolean deleteSubscription(Subscriptions subs) {
		if (!isSubscribedUser(subs.getUser()))
			return false;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.deletePersistent(pm.getObjectById(Subscriptions.class,
					subs.getKey()));
			tx.commit();
		} catch (Exception ex) {

		} finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Met a jour un abonnement dans le Datastore.
	 * 
	 * @param updatedSubs l'abonnement a mettre a jour
	 * @return true si la mise a jour s'est terminee avec succes, false sinon
	 */
	public static boolean updateSubscription(Subscriptions updatedSubs) {
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();

			Subscriptions s = pm.getObjectById(Subscriptions.class,
					updatedSubs.getKey());
			s.setEnabledSubscriptions(updatedSubs.getEnabledSubscriptions());

			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Retourne un abonnement avec sa cle.
	 * 
	 * @param key la cle identifiant l'abonnement
	 * @return l'abonnement s'il est trouve, null sinon
	 */
	public static Subscriptions getSubscriptionByKey(Key key) {
		Subscriptions subs;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			subs = pm.getObjectById(Subscriptions.class, key);
		} catch (Exception ex) {
			subs = null;
		} finally {
			pm.close();
		}

		return subs;
	}

	/**
	 * Retourne l'abonnement d'un utilisateur.
	 * 
	 * @param user l'utilisateur
	 * @return l'abonnement de l'utilisateur s'il existe, null sinon
	 */
	public static Subscriptions getSubscriptionsByUser(User user) {
		if (!isSubscribedUser(user))
			return null;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Subscriptions subs;

		try {
			Query query = pm.newQuery(Subscriptions.class);
			query.setFilter("this.user == user");
			query.declareParameters("com.google.appengine.api.users.User user");

			@SuppressWarnings("unchecked")
			List<Subscriptions> l = (List<Subscriptions>) query.execute(user);

			subs = (Subscriptions) l.get(0);
		} finally {
			pm.close();
		}

		return subs;
	}

	/**
	 * Retourne la liste de tous les abonnements.
	 * 
	 * @return la liste de tous les abonnements
	 */
	@SuppressWarnings("unchecked")
	public static List<Subscriptions> getAllSubscriptions() {
		List<Subscriptions> subs;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Subscriptions.class);
			query.setOrdering("user asc");

			subs = (List<Subscriptions>) query.execute();
		} finally {
			pm.close();
		}

		return subs;
	}

	/**
	 * Retourne la liste de tous les abonnements concernes par un sport.
	 * 
	 * @param sport le sport
	 * @return la liste de tous les abonnements concernes par le sport
	 */
	public static List<User> getSubscribedUsersSport(Sport sport) {
		List<User> subscribedUsers = new ArrayList<>();

		for (Subscriptions s : getAllSubscriptions()) {
			if (s.isEnabled(sport))
				subscribedUsers.add(s.getUser());
		}

		return subscribedUsers;
	}

	/**
	 * Retourne true si un participant existe deja dans le Datastore, false sinon.
	 * 
	 * @param participant le participant
	 * @return true si le participant existe, false sinon
	 */
	public static boolean isExistsParticipant(Participant participant) {
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Participant.class, participant.getKey()) != null);
		} catch (Exception e) {
			isExists = false;
		} finally {
			pm.close();
		}

		return isExists;
	}

	/**
	 * Enregistre un participant dans le Datastore.
	 * 
	 * @param participant le participant a enregistrer
	 * @return true si l'enregistrement s'est terminee avec succes, false sinon
	 */
	public static boolean saveParticipant(Participant participant)
	{
		if (isExistsParticipant(participant))
			return false;
		
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.makePersistent(participant);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Met a jour un participant dans le Datastore.
	 * 
	 * @param updatedParticipant le participant a mettre a jour
	 * @return true si la mise a jour s'est terminee avec succes, false sinon
	 */
	public static boolean updateParticipant(Participant updatedParticipant) {
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();

			Participant p = pm.getObjectById(Participant.class, updatedParticipant.getKey());
			p.setEvents(updatedParticipant.getEvents());

			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				success = false;
			}
			pm.close();
		}

		return success;
	}

	/**
	 * Retourne un participant avec sa cle.
	 * @param key la cle identifiant le participan
	 * @return le participant s'il est trouve, null sinon
	 */
	public static Participant getParticipantByKey(Key key)
	{
		Participant p;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try
		{
			p = pm.getObjectById(Participant.class, key);
		}
		catch (Exception ex) {
			p = null;
			ex.printStackTrace();
		}
		finally {
			pm.close();
		}

		return p;
	}

	/**
	 * Retourne le participant d'un utilisateur
	 * @param user l'utilisateur
	 * @return le participant
	 */
	@SuppressWarnings("unchecked")
	public static Participant getParticipantByUser(User user) {
		Participant p;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Participant.class, "this.user == user");
			query.declareParameters("com.google.appengine.api.users.User user");

			List<Participant> l = (List<Participant>) query.execute(user);

			if (!l.isEmpty())
				p = l.get(0);
			else
				p = null;
		} finally {
			pm.close();
		}

		return p;
	}

	/**
	 * Retourne la liste des participants à un evenement
	 * @param event l'evenement
	 * @return la liste des participants
	 */
	public static List<Participant> getParticipantsByEvent(Event event)
	{
		List<Participant> participants = new ArrayList<>();

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try
		{
			for (Key k : event.getParticipants())
			{
				participants.add(getParticipantByKey(k));
			}
		}
		finally {
			pm.close();
		}

		return participants;
	}
}
