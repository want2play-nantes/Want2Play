package com.want2play.datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.core.Subscriptions;

public class DatastoreController {

	private DatastoreController() {
	}

	/**
	 * Retourne true si un �v�nement existe d�j� dans le datastore, false sinon.
	 * 
	 * @param event
	 *            �v�nement
	 * @return true si l'�v�nement existe d�j� dans le datastore, false sinon
	 */
	public static boolean alreadySavedEvent(Event event)
	{
		PersistenceManager pm = Factory.getInstance().getPersistenceManager();
		boolean notSavedEvent = false;

		try {
			notSavedEvent = (pm.getObjectById(Event.class, event.getKey()) != null);
		} finally {
			pm.close();
		}

		return notSavedEvent;
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
		if (alreadySavedEvent(event))
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
		if (!alreadySavedEvent(event))
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
	public static boolean updateEvent(Event eventToEdit, String place, Date date, String sport, int nbParticipants)
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
			event.setNbParticipants(nbParticipants);

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
	 * Retourne la liste de tous les �v�nements.
	 * 
	 * @return la liste de tous les �v�nements
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
	 * Retourne la liste de tous les �v�nements cr�e par un utilisateur.
	 * 
	 * @param user
	 *            utilisateur
	 * @return la liste de tous les �v�nements cr�e par l'utilisateur
	 */
	@SuppressWarnings("unchecked")
	public static List<Event> getEventsUser(User user)
	{
		List<Event> userEvents = new ArrayList<>();

		PersistenceManager pm = Factory.getInstance().getPersistenceManager();

		try {
			Query query = pm.newQuery(Event.class, "this.creator == creator");
			query.declareParameters("com.google.appengine.api.users.User creator");
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

			System.out.println("Est abonn� ? " + l.size());

			isSubscribed = (l.size() != 0);
		} finally {
			pm.close();
		}

		return isSubscribed;
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

	/**
	 * Retourne l'abonnement d'un utilisateur.
	 * 
	 * @param user
	 *            utilisateur
	 * @return l'abonnement de l'utilisateur s'il existe, null sinon
	 */
	public static Subscriptions getSubscriptionUser(User user)
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
}
