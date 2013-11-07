package com.want2play.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class InfoHeaderUser implements Filter {

	@Override
	public void init(FilterConfig chain) throws ServletException
	{
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
			throws IOException, ServletException
	{
		UserService userService = UserServiceFactory.getUserService();
		
		if(userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();
			req.setAttribute("loginLogoutHref", "/Logout");
			req.setAttribute("loginLogoutButtonLabel", "Déconnexion");
			req.setAttribute("userStatus", "Vous êtes connecté en tant que " + user.getNickname());
		}
		else {
			req.setAttribute("loginLogoutHref", "/Login");
			req.setAttribute("loginLogoutButtonLabel", "Connexion");
			req.setAttribute("userStatus", "Vous êtes déconnecté");
		}
		chain.doFilter(req, resp);
	}
	
	@Override
	public void destroy() {

	}
}
