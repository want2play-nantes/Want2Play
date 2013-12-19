package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.want2play.core.Participant;
import com.want2play.datastore.DatastoreController;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		
		if (!userService.isUserLoggedIn())
		{
			log("Utilisateur inconnu : Redirection vers la page de connexion");
	    	resp.sendRedirect(userService.createLoginURL("/Login"));
	    }
		else
		{
			User user = userService.getCurrentUser();
			log("Utilisateur authentifié : " + user.getEmail());
			
			if (DatastoreController.getParticipantByUser(user) == null)
			{
				DatastoreController.saveParticipant(new Participant(user));
			}
			
			// Amène des problèmes lors de rafraichissements
			//req.getSession().setAttribute("user", user);
			
			resp.sendRedirect("/");
		}
	}
}
