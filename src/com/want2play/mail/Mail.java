package com.want2play.mail;


import javax.mail.internet.*;
import javax.mail.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
	
public class Mail {
	/**
	 * Classe permettant d'envoyer un mail.
	 */

	   private final static String MAILER_VERSION = "Java";
	   public static boolean envoyerMailSMTP(String serveur, boolean debug) throws FileNotFoundException {
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
                 
                 //ajout du fichier html a envoyer
                 
                 File inputFile = new File("src/message.html");
                 FileReader r = new FileReader(inputFile);
                 BufferedReader bf = new BufferedReader(r);
                 StringBuilder mailSb = new StringBuilder();
                 String chaine ; 
                 try{	  
               	  chaine = bf.readLine();
               	  do{ 	 
               	  	 mailSb.append(chaine);
               	  	 chaine = bf.readLine();
               	  	}while(chaine != null);
                 }
                 catch (IOException exception)
                 {
                     System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
                 }
                 
                 String mailStr = new String(mailSb);
                 String res1 = mailStr.replaceAll("sport", "nomination_sport");
                 String res2 = res1.replaceAll("date", "date_courante");
                 String res3 = res2.replaceAll("heure", "heure_courante");
                 String res4 = res3.replaceAll("lieu", "lieu_ciblé");
                 String res5 = res4.replaceAll("nbParticipants", "nb_part");
                 
                 message.setContent(res5, "text/html; charset=utf-8");
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
	   
	   public static void main(String[] args) throws FileNotFoundException {
	         Mail.envoyerMailSMTP("127.0.0.1",true);
	   }
	}