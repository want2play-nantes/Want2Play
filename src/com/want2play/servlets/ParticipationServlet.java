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
import com.want2play.core.Participant;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class ParticipationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		try {
		
			User user = UserServiceFactory.getUserService().getCurrentUser();
			Event event = DatastoreController.getEventByKey(KeyFactory.stringToKey(req.getParameter("event")));
			Participant participant = DatastoreController.getParticipantByUser(user);
			
			if (req.getParameter("mode").equals("delete"))
			{
				participant.removeEvent(event);
				DatastoreController.updateParticipant(participant);
				DatastoreController.updateEvent(event);
				log("Participation supprimée");
			}
			else if (req.getParameter("mode").equals("add"))
			{
				if (participant == null)
				{
					participant = new Participant(user);
					participant.addEvent(event);
					DatastoreController.saveParticipant(participant);
					DatastoreController.updateEvent(event);
					log("Participation crée");
				}
				else
				{
					participant.addEvent(event);
					DatastoreController.updateParticipant(participant);
					DatastoreController.updateEvent(event);
					log("Participation update");
				}
			}
			else {
				log("Rien n'est fait");
			}
		}
		catch(Exception e) {
			log("Erreur de paramètre");
		}
		
		resp.sendRedirect(req.getHeader("Referer"));
	}
}
