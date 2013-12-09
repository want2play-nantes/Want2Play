package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.want2play.core.Event;
import com.want2play.datastore.DatastoreController;
import com.want2play.mail.Mail;

public class DeleteEventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Key key = KeyFactory.stringToKey(req.getParameter("event"));

		Event eToDel = DatastoreController.getEventByKey(key);

		Boolean success = DatastoreController.deleteEvent(eToDel);
		Boolean successM = Mail.sendMailCancelledEvent(getServletContext(),
				eToDel);

		if (success) {

			log("Suppression de l'évènement :" + success + " | Envoie mail: "
					+ successM);

		} else {

			log("Aucun évènement ne porte cette clé :" + key);

		}
		// resp.sendRedirect("/");
		resp.sendRedirect(req.getHeader("Referer"));

	}

}
