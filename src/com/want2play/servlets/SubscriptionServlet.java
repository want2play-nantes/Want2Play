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
import com.want2play.core.Subscriptions;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class SubscriptionServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
        PrintWriter out = resp.getWriter();
		
        Subscriptions subs ;
		if (DatastoreController.isSubscribedUser(user)){
			subs = DatastoreController.getSubscriptionsByUser(user);
		}else{ subs = new Subscriptions(user);}
			 
		req.setAttribute("subs",subs);
		getServletContext().getRequestDispatcher("/WEB-INF/subscription.jsp").forward(req, resp);
	}
}
