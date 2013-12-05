package com.want2play.servlets;

import java.io.IOException;
import java.io.PrintWriter;

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
public class SubscriptionSaveServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PrintWriter out = resp.getWriter();

		Subscriptions subs;
		if (DatastoreController.isSubscribedUser(user)) {
			subs = DatastoreController.getSubscriptionsByUser(user);
			for (Sport s : Sport.values()) {
				Boolean value = Boolean.parseBoolean(req.getParameter(s
						.getLabel()));
				subs.setEnabled(s, value);
				
			}
			// DatastoreController.updateSubscription(subs);
		} else {
			subs = new Subscriptions(user);
			for (Sport s : Sport.values()) {
				out.println("hello");
				Boolean value = Boolean.parseBoolean(req.getParameter(s
						.getLabel()));
				subs.setEnabled(s, value);

			}
			DatastoreController.saveSubscription(subs);
		}
		resp.sendRedirect("/Subscription");
	}
}
