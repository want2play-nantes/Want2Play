package com.want2play.mail;
import javax.mail.internet.*;
	import javax.mail.*;
	import java.util.*;
	
public class Mail {
	/**
	 * Classe permettant d'envoyer un mail.
	 */

	   private final static String MAILER_VERSION = "Java";
	   public static boolean envoyerMailSMTP(String serveur, boolean debug) {
	         boolean result = false;
	         try {
	                  Properties prop = System.getProperties();
	                  prop.put("mail.smtp.starttls.enable", "true"); 
	                  prop.put("mail.smtp.host", "smtp.gmail.com");


	                  prop.put("mail.smtp.port", "587");
	                  prop.put("mail.smtp.auth", "true");
	                  
	                  Session session = Session.getInstance(prop,(new GMailAuthenticator("adm.want2play@gmail.com","3r9W2Nrx")));
	                  
	                  Message message = new MimeMessage(session);
	                  message.setFrom(new InternetAddress("adm.want2play@gmail.com"));
	                  InternetAddress[] internetAddresses = new InternetAddress[1];
	                  internetAddresses[0] = new InternetAddress("agnesgnagne@gmail.com");
	                  message.setRecipients(Message.RecipientType.TO,internetAddresses);
	                  message.setSubject("Notification");
	                  message.setText("Nouvel Evenement Crée : ........."+(new Date()));
	                  message.setHeader("X-Mailer", MAILER_VERSION);
	                  message.setSentDate(new Date());
	                  session.setDebug(debug);
	                  Transport.send(message);
	                  result = true;
	         } catch (AddressException e) {
	                  e.printStackTrace();
	         } catch (MessagingException e) {
	                  e.printStackTrace();
	         }
	         return result;
	   }
	   
	   public static void main(String[] args) {
	         Mail.envoyerMailSMTP("127.0.0.1",true);
	   }
	}