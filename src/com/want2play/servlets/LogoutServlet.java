package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		
		log("Deconnexion : " + userService.getCurrentUser().getEmail());
		req.getSession().invalidate();
		resp.sendRedirect(userService.createLogoutURL("/"));
	}
	
}
