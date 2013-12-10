package com.want2play.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Event;
import com.want2play.core.Participant;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Participant participant = DatastoreController.getParticipantByUser(user);
		
		if (userService.isUserLoggedIn())
		{
			req.setAttribute("eventUser", eventsSortedByDate(DatastoreController.getEventsByUser(user)));

			List<Event> participations = new ArrayList<>();
			
			if (participant != null)
			{
				for (Key k : participant.getEvents())
				{
					participations.add(DatastoreController.getEventByKey(k));
				}
			}
			
			req.setAttribute("participationsUser", eventsSortedByDate(participations));
		}
		
		List<Event> allEvents;
		
		if (userService.isUserLoggedIn())
		{
			allEvents = DatastoreController.getEventsWithoutUser(user);
		}
		else
		{
			allEvents = DatastoreController.getAllEvents();
		}
		
		req.setAttribute("allEvents", eventsSortedByDate(allEvents));
		
		getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
	
	private TreeMap<DateTime, List<Event>> eventsSortedByDate(List<Event> events)
	{
		Collections.sort(events);
		
		TreeMap<DateTime, List<Event>> eventUserSortedByDate = new TreeMap<>();
		
		
		
		for (Event e: events)
		{
			// Si la date est après l'heure actuelle - 15mn
			if (e.getFullDate().isAfter(DateTime.now().minusMinutes(15))) {
				if (!eventUserSortedByDate.containsKey(e.getDate()))
				{
					eventUserSortedByDate.put(e.getDate(), new ArrayList<Event>());
				}
				eventUserSortedByDate.get(e.getDate()).add(e);
			}
		}
		
		return eventUserSortedByDate;
	}
}