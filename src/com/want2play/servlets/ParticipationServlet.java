package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Event;
import com.want2play.core.Participation;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class ParticipationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		if (req.getParameter("participation") != null)
		{
			Key key = KeyFactory.stringToKey(req.getParameter("participation"));
			
			Participation participation = DatastoreController.getParticipationByKey(key);
			
			if (participation != null) {
				DatastoreController.deleteParticipation(participation);
				log("Participation supprimée");
			}
			else {
				log("Participation pas supprimée");
			}
		}
		else if (req.getParameter("event") != null)
		{
			User user = UserServiceFactory.getUserService().getCurrentUser();
			
			Key key = KeyFactory.stringToKey(req.getParameter("event"));
			Event event = DatastoreController.getEventByKey(key);
			
			Participation participation = new Participation(user, event);
			
			if (!DatastoreController.isExistsParticipation(participation))
			{
				DatastoreController.saveParticipation(participation);
				log("Participation crée");
			}
			else {
				log("Participation pas crée");
			}
		}
		else {
			log("Rien n'est fait");
		}
		
		resp.sendRedirect(req.getHeader("Referer"));
	}
}
