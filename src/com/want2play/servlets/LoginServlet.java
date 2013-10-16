package com.want2play.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		HttpSession session = req.getSession(true);
		
		if (!userService.isUserLoggedIn())
		{
			session.setAttribute("referer", req.getHeader("Referer"));
	    	resp.sendRedirect(userService.createLoginURL("/Login"));
	    }
		else {
			String refererURI = session.getAttribute("referer").toString();
			
			session.removeAttribute("referer");
			session.setAttribute("user", userService.getCurrentUser());
			
			resp.sendRedirect(refererURI);
		}
	}
}
