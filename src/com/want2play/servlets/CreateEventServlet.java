package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.datastore.DatastoreController;
import com.want2play.mail.Mail;

public class CreateEventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			Event evt = null;
			User creator = (User) req.getAttribute("user");

			DateTime date = DateTime.parse(req.getParameter("date"),
					DateTimeFormat.forPattern("yyyy-MM-dd"));
			DateTime hour = DateTime.parse(req.getParameter("heure"),
					DateTimeFormat.forPattern("H:mm"));

			String place = req.getParameter("lieu");

			Integer nbPart = Integer.valueOf(req.getParameter("nbPart"));

			if (req.getParameter("mod").contains("edit")) {
				evt = DatastoreController.getEventByKey(KeyFactory
						.stringToKey(req.getParameter("keyStr")));
				evt.setDate(date);
				evt.setHour(hour);
				evt.setNbParticipantsMax(nbPart);
				evt.setPlace(place);

				Boolean success = DatastoreController.updateEvent(evt);
				Boolean successM = false;
				if (success)
					successM = Mail.sendMailEditEvent(getServletContext(), evt);

				log("Edition de l'évènement: " + success + " | Envoi mail: " + successM);
			} else {
				if (req.getParameter("mod").equals("new")) {
					Sport sport = Sport.valueOf(req.getParameter("sport"));

					evt = new Event(creator, date, hour, place, sport, nbPart);
					Boolean success = DatastoreController.saveEvent(evt);
					Boolean successM = Mail.sendMailNewEvent(
							getServletContext(), evt);
					log("Création de l'évènement: " + success
							+ " | Envoie mail: " + successM);
				} else {
					log("Aucun évènement n'a été traité");
				}
			}
		} catch (Exception ex) {
			log("Erreur de paramètre");
		}

		resp.sendRedirect("/");

	}

}
