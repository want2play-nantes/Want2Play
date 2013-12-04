package com.want2play.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		
		if (userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();
			
			//Event event = new Event(user, "Angers", new Date(), Sport.BASKETBALL, 2);
			//if (!DatastoreController.alreadySavedEvent(event))
			//	DatastoreController.saveEvent(event);
			
			List<Event> allUserEvents = DatastoreController.getEventsUser(user);
			req.setAttribute("eventUser", allUserEvents);
			
			List<Event> allUserEventParticipate = DatastoreController.getEventParticipations(user);
			req.setAttribute("participationsUser", allUserEventParticipate);
		}
		
		List<Event> allEvents = DatastoreController.getAllEvents();
		req.setAttribute("allEvents", allEvents);
		
		getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
	
}
