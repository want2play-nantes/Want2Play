package com.want2play.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.core.Participation;
import com.want2play.core.Sport;
import com.want2play.core.Subscriptions;

public class DatastoreController {

	private DatastoreController() {
	}

	/**
	 * Retourne true si un evenement existe deja dans le datastore, false sinon.
	 * 
	 * @param event evenement
	 * @return true si l'evenement existe deja dans le datastore, false sinon
	 */
	public static boolean isExistsEvent(Event event)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Event.class, event.getKey()) != null);
		}
		catch (Exception e) {
			isExists = false;
		}
		finally {
			pm.close();
		}

		return isExists;
	}

	/**
	 * Enregistre un �v�nement dans le Datastore.
	 * 
	 * @param event
	 *            �v�nement � enregistrer
	 * @return true si l'enregistrement s'est termin� avec succ�s, false sinon
	 */
	public static boolean saveEvent(Event event)
	{
		if (isExistsEvent(event))
			return false;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.makePersistent(event);
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
	 * Supprime un �v�nement du Datastore.
	 * 
	 * @param event
	 *            �v�nement � suprimmer
	 * @return true si la suppression s'est termin�e avec succ�s, false sinon
	 */
	public static boolean deleteEvent(Event event)
	{
		if (!isExistsEvent(event))
			return false;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.deletePersistent(pm.getObjectById(Event.class, event.getKey()));
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
	 * Met � jour un �v�nement du Datastore.
	 * 
	 * @param eventToEdit
	 *            �v�nement � mettre � jour
	 * @param place
	 *            endroit de l'�v�nement
	 * @param date
	 *            date de l'�v�nement
	 * @param sport
	 *            sport pratiqu� lors de l'�v�nement
	 * @param nbParticipants
	 *            nombre de participants maximum
	 * @return true si la mise � jour s'est termin�e avec succ�s, false sinon
	 */
	public static boolean updateEvent(Event eventToEdit, String place, Date date, Sport sport, int nbParticipantsMax)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();

			Event event = pm.getObjectById(Event.class, eventToEdit.getKey());
			event.setPlace(place);
			event.setDate(date);
			event.setSport(sport);
			event.setNbParticipantsMax(nbParticipantsMax);

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
	
	public static Event getEventByKey(Key key)
	{
		Event event;
		
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
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
	 * Retourne la liste de tous les évènements.
	 * 
	 * @return la liste de tous les évènements
	 */
	@SuppressWarnings("unchecked")
	public static List<Event> getAllEvents()
	{
		List<Event> events;

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Event.class);
			query.setOrdering("date desc");
			
			events = (List<Event>)query.execute();

		} finally {
			pm.close();
		}

		return events;
	}

	/**
	 * Retourne la liste de tous les évènements crée par un utilisateur.
	 * 
	 * @param user
	 *            utilisateur
	 * @return la liste de tous les évènements crée par l'utilisateur
	 */
	@SuppressWarnings("unchecked")
	public static List<Event> getEventsByUser(User user)
	{
		List<Event> userEvents = new ArrayList<>();

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Event.class, "this.creator == user");
			query.declareParameters("com.google.appengine.api.users.User user");
			query.setOrdering("date desc");

			userEvents = (List<Event>) query.execute(user);
		} finally {
			pm.close();
		}

		return userEvents;
	}

	/**
	 * Retourne true si un utilisateur est d�j� abonn�, false sinon.
	 * 
	 * @param user
	 *            utilisateur
	 * @return true si un utilisateur est d�j� abonn�, false sinon
	 */
	public static boolean isSubscribedUser(User user)
	{
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
	
	public static boolean isExistsSubscriptions(Subscriptions subs)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Subscriptions.class, subs.getKey()) != null);
		}
		catch (Exception e) {
			isExists = false;
		}
		finally {
			pm.close();
		}

		return isExists;
	}

	/**
	 * Enregistre un abonnement dans le Datastore.
	 * 
	 * @param subs
	 *            abonnement � enregistrer
	 * @return true si l'enregistrement s'est termin� avec succ�s, false sinon
	 */
	public static boolean saveSubscription(Subscriptions subs)
	{
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
	 * @param subs
	 *            abonnement � supprimer
	 * @return true si la suppression s'est termin�e avec succ�s, false sinon
	 */
	public static boolean deleteSubscription(Subscriptions subs)
	{
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
	 * Met � jour un abonnement dans le Datastore.
	 * 
	 * @param subsToEdit
	 *            abonnement � mettre � jour
	 * @param enabledSubscriptions
	 *            liste d'abonnement
	 * @return true si la mise � jour s'est termin�e avec succ�s, false sinon
	 */
	public static boolean updateSubscription(Subscriptions subsToEdit, HashMap<Sport, Boolean> enabledSubscriptions)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();

			Subscriptions subs = pm.getObjectById(Subscriptions.class, subsToEdit.getKey());

			for (Map.Entry<Sport, Boolean> e : subsToEdit.getEnabledSubscriptions().entrySet()) {
				subs.setEnabled(e.getKey(), e.getValue());
			}

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
	
	public static Subscriptions getSubscriptionByKey(Key key)
	{
		Subscriptions subs;
		
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			subs = pm.getObjectById(Subscriptions.class, key);
		}
		catch (Exception ex) {
			subs = null;
		}
		finally {
			pm.close();
		}

		return subs;
	}

	/**
	 * Retourne l'abonnement d'un utilisateur.
	 * 
	 * @param user
	 *            utilisateur
	 * @return l'abonnement de l'utilisateur s'il existe, null sinon
	 */
	public static Subscriptions getSubscriptionsByUser(User user)
	{
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
	public static List<Subscriptions> getAllSubscriptions()
	{
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
	 * Retourne la liste de tous les abonnements concern�s par un sport.
	 * 
	 * @param sport
	 *            sport
	 * @return la liste de tous les abonnements concern�s par le sport
	 */
	public static List<User> getSubscribedUsersSport(Sport sport)
	{
		List<User> subscribedUsers = new ArrayList<>();

		for (Subscriptions s : getAllSubscriptions()) {
			if (s.isEnabled(sport))
				subscribedUsers.add(s.getUser());
		}

		return subscribedUsers;
	}
	
	public static boolean isExistsParticipation(Participation participation)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean isExists;

		try {
			isExists = (pm.getObjectById(Participation.class, participation.getKey()) != null);
		}
		catch (Exception e) {
			isExists = false;
		}
		finally {
			pm.close();
		}

		return isExists;
	}
	
	public static boolean saveParticipation(Participation participation)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.makePersistent(participation);
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
	
	public static boolean deleteParticipation(Participation participation)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		boolean success = true;

		try {
			tx.begin();
			pm.deletePersistent(pm.getObjectById(Participation.class, participation.getKey()));
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
	
	public static Participation getParticipationByKey(Key key)
	{
		Participation participation;
		
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			participation = pm.getObjectById(Participation.class, key);
		}
		catch (Exception ex) {
			participation = null;
		}
		finally {
			pm.close();
		}

		return participation;
	}

	@SuppressWarnings("unchecked")
	public static List<Participation> getParticipationsByUser(User user)
	{
		List<Participation> userParticipations = new ArrayList<>();

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Participation.class, "this.user == user");
			query.declareParameters("com.google.appengine.api.users.User user");

			userParticipations = (List<Participation>) query.execute(user);
		}
		finally {
			pm.close();
		}
		
		Collections.sort(userParticipations);

		return userParticipations;
	}
}
