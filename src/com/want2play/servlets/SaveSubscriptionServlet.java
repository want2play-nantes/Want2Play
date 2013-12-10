package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Sport;
import com.want2play.core.Subscriptions;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class SaveSubscriptionServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Subscriptions subs;

		if (DatastoreController.isSubscribedUser(user)) {
			subs = DatastoreController.getSubscriptionsByUser(user);

			processParameters(req, subs);

			log("Abonnement : Mise à jour dans le DS");
			DatastoreController.updateSubscription(subs);
		} else {
			subs = new Subscriptions(user);

			processParameters(req, subs);

			log("Abonnement : Enregistrement dans le DS");
			DatastoreController.saveSubscription(subs);
		}
		resp.sendRedirect("/Subscription?resp=1");
	}

	private Subscriptions processParameters(HttpServletRequest req,
			Subscriptions subs) {
		for (Sport s : Sport.values()) {
			if (req.getParameter(s.getLabel()) != null)
				subs.setEnabled(s, true);
			else
				subs.setEnabled(s, false);
		}

		return subs;

	}
}
