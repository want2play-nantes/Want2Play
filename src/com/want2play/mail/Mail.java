package com.want2play.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import com.want2play.core.Participant;
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
	 * @throws Exception
	 */
	private static boolean sendMail(ServletContext sc, List<User> users, Event event, String subject, String content) {

		// Paramètres du serveur SMTP
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try
		{
			// Création du message
			Message message = new MimeMessage(session);

			// Emetteur (doit être l'administrateur du site)
			message.setFrom(new InternetAddress("adm.want2play@gmail.com", "Want2Play"));

			if (users.isEmpty())
			{
				sc.log("Aucun destinataire pour la notification de " + subject);
				return false;
			}

			// Destinataire
			for (User u : users) {
				if (!event.getCreator().equals(u)) {
					sc.log(u.getEmail());
					message.addRecipient(Message.RecipientType.BCC,	new InternetAddress(u.getEmail()));
				}
			}

			// Sujet du mail
			message.setSubject(subject);

			// Corps du message
			message.setContent(content, "text/html; charset=utf-8");

			Transport.send(message);
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public static boolean sendMailNewEvent(ServletContext sc, Event e)
	{
		String content = readFile(sc, "newEvent.html");

		content = content.replace("#sport#", e.getSport().getLabel());
		content = content.replace("#date#", e.getDateStr());
		content = content.replace("#heure#", e.getHourStr());
		content = content.replace("#lieu#", e.getPlace());
		content = content.replace("#nbParticipants#", e.getNbParticipantsMax()
				.toString());

		String subject = "Want2Play : Nouvelle activite !";

		return sendMail(sc, DatastoreController.getSubscribedUsersSport(e.getSport()), e, subject, content);
	}

	public static boolean sendMailCancelledEvent(ServletContext sc, Event e)
	{
		String content = readFile(sc, "deletedEvent.html");

		content = content.replace("#sport#", e.getSport().getLabel());
		content = content.replace("#date#", e.getDateStr());
		content = content.replace("#heure#", e.getHourStr());
		content = content.replace("#lieu#", e.getPlace());

		String subject = "Want2Play : Activite annulee !";

		List<User> users = new ArrayList<>();
		for (Participant p : DatastoreController.getParticipantsByEvent(e)) {
			users.add(p.getUser());
		}

		return sendMail(sc, users, e, subject, content);
	}

	public static boolean sendMailEditEvent(ServletContext sc, Event e)
	{
		String content = readFile(sc, "editEvent.html");

		content = content.replace("#sport#", e.getSport().getLabel());
		content = content.replace("#date#", e.getDateStr());
		content = content.replace("#heure#", e.getHourStr());
		content = content.replace("#lieu#", e.getPlace());

		String subject = "Want2Play : Activite modifiee !";
		
		List<User> users = new ArrayList<>();
		for (Participant p : DatastoreController.getParticipantsByEvent(e))
		{
			sc.log(p.toString());
			users.add(p.getUser());
		}

		return sendMail(sc, users, e, subject, content);
	}

	private static String readFile(ServletContext sc, String filename)
	{
		StringBuilder mailSb = new StringBuilder();

		try (BufferedReader bf = new BufferedReader(new InputStreamReader(sc.getResourceAsStream(MAIL_TEMPLATE_FOLDER + filename)))) {
			String chaine = bf.readLine();

			do {
				mailSb.append(chaine);
				chaine = bf.readLine();
			} while (chaine != null);
		} catch (IOException ex) {
		}

		return mailSb.toString();
	}
}