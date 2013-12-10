package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class EventServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		if (req.getParameter("mode") != null) {
			
			if (req.getParameter("mode").equals("edit") && req.getParameter("event") != null)
			{
				Key key = KeyFactory.stringToKey(req.getParameter("event"));
				
				Event event = DatastoreController.getEventByKey(key);
				log(event.getParticipants().toString());
				
				if (event != null)
				{
					req.setAttribute("event", event);
				}
			
			}
		}
		
		req.setAttribute("sportsList", Sport.sortedValues());
		
		
		getServletContext().getRequestDispatcher("/WEB-INF/event.jsp").forward(req, resp);
	}
	
}
