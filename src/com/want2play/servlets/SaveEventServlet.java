package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.datastore.DatastoreController;
import com.want2play.mail.Mail;

public class SaveEventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		if (req.getParameter("mode") != null) 
		{
			Event event = null;
			
			if (req.getParameter("mode").equals("edit") || req.getParameter("mode").equals("new"))
			{
				User creator = UserServiceFactory.getUserService().getCurrentUser();
				
				DateTime date = DateTime.parse(req.getParameter("date"), DateTimeFormat.forPattern("dd/MM/yyyy"));
				DateTime hour = DateTime.parse(req.getParameter("heure"), DateTimeFormat.forPattern("H:mm"));
				
				if (date.isBefore(DateTime.now())) {
					resp.sendRedirect("/");
				}
				
				String place = req.getParameter("lieu");
				
				Integer nbPart = Integer.valueOf(req.getParameter("nbPart"));
			
				if (req.getParameter("mode").contains("edit"))
				{
					event = DatastoreController.getEventByKey(KeyFactory.stringToKey(req.getParameter("event")));
					event.setDate(date);
					event.setHour(hour);
					event.setNbParticipantsMax(nbPart);
					event.setPlace(place);
					
					if (DatastoreController.updateEvent(event))
					{
						boolean mailSuccess = Mail.sendMailEditEvent(getServletContext(), event);
						log("Mise à jour de l'évènement: " + true + " | Envoi mail: " + mailSuccess);
					}
					else
					{
						log("Echec de la mise à jour de l'évènement");
					}
				}
				else if (req.getParameter("mode").equals("new"))
				{
					Sport sport = Sport.valueOf(req.getParameter("sport"));
					
					event = new Event(creator, date, hour, place, sport, nbPart);
					
					if (DatastoreController.saveEvent(event))
					{
						boolean mailSuccess = Mail.sendMailNewEvent(getServletContext(), event);
						log("Création de l'évènement: " + true + " | Envoi mail: " + mailSuccess);
					}
					else
					{
						log("Echec de l'enregistrement de l'évènement");
					}
				}
			}
			else if (req.getParameter("mode").equals("delete"))
			{
				Key key = KeyFactory.stringToKey(req.getParameter("event"));
				
				event = DatastoreController.getEventByKey(key);
				
				boolean mailSuccess = Mail.sendMailCancelledEvent(getServletContext(), event);
				
				if (DatastoreController.deleteEvent(event))
				{
					log("Suppression de l'évènement :" + true + " | Envoi mail: "+ mailSuccess);					
				}
				else
				{
					log("Echec de la suppression de l'évènement");
				}
			}
			else
			{
				log("Aucun évènement n'a été traité");
			}
		}

		resp.sendRedirect("/");

	}

}
