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


public class DeleteEventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{	
		
		Key key = KeyFactory.stringToKey(req.getParameter("event"));		
		
		Event eToDel = DatastoreController.getEventByKey(key);
				
		Boolean success = DatastoreController.deleteEvent(eToDel);
		
		if(success){
		
			log("Suppression de l'évènement :"+success.toString());
		
		}
		else{
			
			log("Aucun évènement ne porte cette clé :"+key.toString());
		
		}	
		//resp.sendRedirect("/");
		resp.sendRedirect(req.getHeader("Referer"));	    	
		 
	}

}
