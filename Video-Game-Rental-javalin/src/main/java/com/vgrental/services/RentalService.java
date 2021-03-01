package com.vgrental.services;

import java.util.Properties;
import java.util.Stack;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.vgrental.models.Game;
import com.vgrental.models.Rental;
import com.vgrental.models.User;
import com.vgrental.repositories.GameDAO;
import com.vgrental.repositories.IGameDAO;
import com.vgrental.repositories.IRentalDAO;
import com.vgrental.repositories.IUserDAO;
import com.vgrental.repositories.RentalDAO;
import com.vgrental.repositories.UserDAO;

public class RentalService implements IRentalService {
	private IRentalDAO rentalDAO = new RentalDAO();
	private IGameDAO gameDAO = new GameDAO();
	private IUserDAO userDAO = new UserDAO();
	
	private static final Logger log = LoggerFactory.getLogger(RentalService.class);

	public Rental findRental(int userId, int gameId) {
		Rental r = new Rental(userId, gameId);
		return rentalDAO.findRental(r);
	}

	public Stack<Object> findUserRentals(User u) {
		return rentalDAO.findUserRentals(u);
	}

	public Stack<Object> findUserRentalGameData(User u) {
		return rentalDAO.findUserRentalGameData(u);
	}

	public boolean rentGame(Rental r) {
		// Grab game and check if available
		Game g = gameDAO.findByGameId(r.getGameId());
		if(g.isAvailable()) {
			g.setAvailable(false); // Set game to unavailable
			
			// Logging
			log.info("Game rented");
			MDC.put("rentGameId", Integer.toString(r.getGameId()));
			MDC.put("rentUserId", Integer.toString(r.getUserId()));
			
			return (rentalDAO.insert(r) && (gameDAO.update(g))); // Update the game's availability and create a new rental
		}
		
		return false;
	}

	public boolean returnGame(Rental r) {
		// Grab game and set to available
		Game g = gameDAO.findByGameId(r.getGameId());
		
		// Make sure game is taken out before making it available
		if(!g.isAvailable()) {
			g.setAvailable(true);
			return (rentalDAO.delete(r) && (gameDAO.update(g))); // Delete rental and set game to available
		}
		
		return false;
	}

	public boolean changeDueDate(Rental r, String newDueDate) {
		r.setDueDate(newDueDate);
		return rentalDAO.update(r);
	}

	public void sendEmail(Rental r) {
		// Grab user's email
		User u = userDAO.findById(r.getUserId());
		// String destinationEmail = u.getEmail();
		String destinationEmail = "ernest.kim@revature.net";
		
		// Vendor's email
		String senderEmail = "ernest.kim@revature.net";
		
		// SMTP Service login credentials
		String uname = System.getenv("AWS_SES_USERNAME");
		String pwd = System.getenv("AWS_SES_PASSWORD");
		String smtphost = System.getenv("AWS_SES_HOST");

		// Properties for session
		Properties propvls = new Properties();
		propvls.put("mail.smtp.auth", "true");
		propvls.put("mail.smtp.starttls.enable", "true");
		propvls.put("mail.smtp.host", smtphost);
		propvls.put("mail.smtp.port", "587");
		
		// Session for MimeMessage
		Session sessionobj = Session.getInstance(propvls, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(uname, pwd);
			}
		});
		
		// Subject and Body of email
		Game g = gameDAO.findByGameId(r.getGameId());
		String username = u.getUsername();
		String dueDate = r.getDueDate();
		String gameName = g.getName();
		String console = g.getConsole().toString();
		String subject = "Your video game rental is overdue!";
		String body = username + ", your video game rental is now overdue.\n"
				+ "Please return " + gameName + " for the " + console + " as soon as possible.\n"
				+ gameName + " for the " + console + " was due on " + dueDate + ".";
		

		try {
			// Create MimeMessage object & set values
			Message messageobj = new MimeMessage(sessionobj);
			messageobj.setFrom(new InternetAddress(senderEmail));
			messageobj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationEmail));
			messageobj.setSubject(subject);
			messageobj.setText(body);
			
			// Now send the message
			Transport.send(messageobj);
			
			// Logging
			log.info("Email successfully sent");
			MDC.put("email", "1");
		} catch (MessagingException exp) {
			throw new RuntimeException(exp);
		}
	}

	public boolean toggleOverdue(Rental r) {
		r.setOverDue(!r.isOverDue()); // Toggle overdue
		
		// If overdue then log and send email
		if (r.isOverDue()) {
			// Logging
			log.info("Game is overdue");
			MDC.put("overdueGameId", Integer.toString(r.getGameId()));
			MDC.put("overdueUserId", Integer.toString(r.getUserId()));
			this.sendEmail(r);
		}

		return rentalDAO.update(r); // Update rental to overdue or not
	}
}
