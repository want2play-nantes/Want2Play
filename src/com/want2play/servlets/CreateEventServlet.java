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
import com.google.appengine.api.datastore.KeyFactory;
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
		
		Event evt = null;
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
		log(place);
		
		Sport sport = Sport.valueOf(req.getParameter("sport").toUpperCase());
		
		Integer nbPart = Integer.valueOf(req.getParameter("nbPart"));

		if(req.getParameter("mod").contains("edit")){
			evt = DatastoreController.getEventByKey(KeyFactory.stringToKey(req.getParameter("keyStr")));
			evt.setDate(date);
			evt.setNbParticipantsMax(nbPart);
			evt.setPlace(place);
			evt.setSport(sport);
			Boolean success = DatastoreController.updateEvent(evt);
			Boolean successM = Mail.sendMailNewEvent(getServletContext(), evt);
			log("Edition de l'évènement: "+success+" | Envoie mail: "+successM);
		
		}else{ 
				if (req.getParameter("mod").equals("new")){
					evt =new Event( creator, place, date, sport, nbPart);
					Boolean success = DatastoreController.saveEvent(evt);
					Boolean successM = Mail.sendMailNewEvent(getServletContext(), evt);
					log("Création de l'évènement: "+success+" | Envoie mail: "+successM);
				}else{
						log("Aucun évènement n'a été traité");
				}		
		}
		
		resp.sendRedirect("/");
	    	
	}

}
