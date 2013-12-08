package com.want2play.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserServiceFactory;

public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig chain) throws ServletException
	{
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
			throws IOException, ServletException
	{
		if(!UserServiceFactory.getUserService().isUserLoggedIn()) {
			HttpServletResponse response = (HttpServletResponse) resp;
			
			response.sendRedirect("/");
		}
		else {
		}
		chain.doFilter(req, resp);
	}
	
	@Override
	public void destroy() {

	}
}
