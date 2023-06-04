package com.automation.utility.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This is a Apache Mail class has a method to send email
 * @author reazur.rahman
 *
 */

public class ApacheMail {
	
	/**
	 * 
	 * @param from From who we are sending the email
	 * @param password The password for the email account
	 * @param to Who is the email going out to
	 * @param host Host name for the mail exchange server
	 * @param subject Subject of the mail
	 * @param testStatus Body of the email
	 * @param cc Who will be cc'ed in this email
	 * @param attachFileName The test report location for attachment to the mail
	 */
	
	
	
	public void sendMail(String from, String password, String to, String host, String subject, String testStatus, String cc, String attachFileName){
	
		 // Get system properties
		   Properties properties = System.getProperties();
		   // Setup mail server
		   properties.put("mail.smtp.starttls.enable", "true");
		   properties.put("mail.smtp.host", host);
		   properties.put("mail.smtp.user", from);
		   properties.put("mail.smtp.password", password);
		   properties.put("mail.smtp.port", "25");
		   properties.put("mail.smtp.auth", "true");
		   				   
		   Session session = Session.getDefaultInstance(properties);

		   try{
			   
			   // Create a default MimeMessage object.
		         Message message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(to));
		         
		         message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));

		         // Set Subject: header field
		         message.setSubject(subject);

		         // Create the message part
		         BodyPart messageBodyPart = new MimeBodyPart();

		         // Now set the actual message
		         messageBodyPart.setText(testStatus);

		         // Create a multipar message
		         Multipart multipart = new MimeMultipart();

		         // Set text message part
		         multipart.addBodyPart(messageBodyPart);

		         // Part two is attachment
		         messageBodyPart = new MimeBodyPart();
		         DataSource source = new FileDataSource(attachFileName);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(attachFileName);
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);

		         // Send message
			      Transport transport = session.getTransport("smtp");
			      transport.connect(host, from, password);
			      transport.sendMessage(message, message.getAllRecipients());
			      transport.close();
			      System.out.println("Sent message successfully....");
	
		      
		
		   }catch (MessagingException mex) {
			      mex.printStackTrace();
			   }
		      
		      
	}
	/**
	 * @param multipart
	 * @param filename
	 */
	
	private static void addAttachment(Multipart multipart, String filename)
	{
	    DataSource source = new FileDataSource(filename);
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    try {
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
	

}