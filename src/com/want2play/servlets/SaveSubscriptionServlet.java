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
		
		boolean updateSuccess = false, saveSuccess = false;

		if (DatastoreController.isSubscribedUser(user))
		{
			subs = DatastoreController.getSubscriptionsByUser(user);

			processParameters(req, subs);

			updateSuccess = DatastoreController.updateSubscription(subs);
			
			log("Mise à jour de l'abonnement (" + subs.getKey() + ") : " + (updateSuccess ? "Success" : "Failed"));
		}
		else
		{
			subs = new Subscriptions(user);
			processParameters(req, subs);

			saveSuccess = DatastoreController.saveSubscription(subs);
			
			log("Création de l'abonnement (" + subs.getKey() + ") : " + (saveSuccess ? "Success" : "Failed"));
		}
		
		resp.sendRedirect("/Subscription?" + ((saveSuccess || updateSuccess) ? "resp=1" : "resp=0"));
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
