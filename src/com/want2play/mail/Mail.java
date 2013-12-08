package com.want2play.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import com.google.appengine.api.users.User;
import com.want2play.core.Event;
import com.want2play.datastore.DatastoreController;

/**
 * Classe permettant d'envoyer un mail.
 */
public class Mail {

	private final static String MAIL_TEMPLATE_FOLDER = "/WEB-INF/mailTemplate/";

	/**
	 * 
	 * @param users
	 * @param subject
	 * @param content
	 * @return
	 */
	private static boolean sendMail(ServletContext sc, List<User> users, Event event, String subject, String content) {

		boolean result;


			// Paramètres du serveur SMTP
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			
			try {
			
			// Création du message
			Message message = new MimeMessage(session);

			// Emetteur (doit être l'administrateur du site)
			message.setFrom(new InternetAddress("adm.want2play@gmail.com", "Want2Play"));

			// Destinataire
			for (User u : users) {
				if (!event.getCreator().equals(u))
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(u.getEmail()));
			}
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("y.vernageau@gmail.com"));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("sumkil17@gmail.com"));

			// Sujet du mail
			message.setSubject(subject);
			
			// Contenu du mail
			//message.setText(content);

			message.setContent(content, "text/html; charset=utf-8");
			
			//message.saveChanges();
			
			Transport.send(message);
			result = true;
		}
		catch (Exception e)
		{
			sc.log(e.toString());
			result = false;
		}
		
		return result;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static boolean sendMailNewEvent(ServletContext sc, Event e) {
		StringBuilder mailSb = new StringBuilder();

		try (BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getResourceAsStream(MAIL_TEMPLATE_FOLDER + "newEvent.html")))) {
			String chaine = bf.readLine();

			do {
				mailSb.append(chaine);
				chaine = bf.readLine();
			} while (chaine != null);
		} catch (IOException ex) {

		}

		String content = mailSb.toString();

		content = content.replace("#sport#", e.getSport().getLabel());
		content = content.replace("#date#", e.getDateStr());
		content = content.replace("#heure#", e.getHourStr());
		content = content.replace("#lieu#", e.getPlace());
		content = content.replace("#nbParticipants#", e.getNbParticipantsMax().toString());
		
		String subject = "Want2Play : Nouvel evenement !";

		return sendMail(sc, DatastoreController.getSubscribedUsersSport(e.getSport()), e, subject, content);
	}
}