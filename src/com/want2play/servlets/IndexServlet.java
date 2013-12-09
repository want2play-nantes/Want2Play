package com.want2play.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			
			if (participant != null) {
				for (Key k : participant.getEvents()) {
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
	
	private HashMap<String, List<Event>> eventsSortedByDate(List<Event> events)
	{
		HashMap<String, List<Event>> eventUserSortedByDate = new HashMap<>();
		
		for (Event e: events)
		{
			if (!eventUserSortedByDate.containsKey(e.getDateStr().toUpperCase()))
			{
				eventUserSortedByDate.put(e.getDateStr().toUpperCase(), new ArrayList<Event>());
			}
			eventUserSortedByDate.get(e.getDateStr().toUpperCase()).add(e);
		}
		
		return eventUserSortedByDate;
	}
}