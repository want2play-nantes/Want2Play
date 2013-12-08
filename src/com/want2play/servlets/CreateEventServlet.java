package com.want2play.servlets;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.core.Sport;
import com.want2play.datastore.DatastoreController;
import com.want2play.mail.Mail;

public class CreateEventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{	
		
		User creator = (User)req.getAttribute("user");
		
		Date date= null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Time time = Time.valueOf(req.getParameter("heure").concat(":00"));
		
		try {
			date = formatter.parse(req.getParameter("date"));
			date.setTime(time.getTime());
						
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String place = req.getParameter("lieu");
		
		Sport sport = Sport.valueOf(req.getParameter("sport").toUpperCase());
		
		Integer nbPart = Integer.valueOf(req.getParameter("nbPart"));

		
		Event evt = new Event( creator, place, date, sport, nbPart);	
		Boolean success = DatastoreController.saveEvent(evt);
		
		Mail.sendMailNewEvent(getServletContext(), evt);

		log("Création de l'évènement :"+success.toString());
		
		resp.sendRedirect("/");
	    	
	}

}
