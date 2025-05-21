package com.carnage.service;

import com.carnage.dao.userDAO.UserDAO;
import com.carnage.model.user.User;
import com.carnage.model.user.client.Client;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Service for sending formal and detailed emails via Gmail SMTP with an App Password.
 */
public class EmailNotificationService {

    private final UserDAO userDAO;
    private final Session mailSession;

    // ─── CONFIGURA AQUÍ TUS CREDENCIALES ─────────────────────────────────────
    private static final String GMAIL_USER         = "migue.626@gmail.com";
    private static final String GMAIL_APP_PASSWORD = "brss yken fjiu esjp".replace(" ", "");
    private static final String FROM_EMAIL         = GMAIL_USER;
    // ─────────────────────────────────────────────────────────────────────────

    public EmailNotificationService(UserDAO userDAO) {
        this.userDAO = userDAO;
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");

        this.mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, GMAIL_APP_PASSWORD);
            }
        });
    }

    /**
     * Sends a formal welcome email after client registration.
     */
    public void notifyRegistration(String email)
            throws DAOException, EntityNotFoundException, MessagingException {
        User user = userDAO.findByEmail(email);
        if (!(user instanceof Client)) {
            throw new EntityNotFoundException("User is not a client: " + email);
        }
        String name = ((Client) user).getUserName();
        String subject = "Welcome to Macota, " + name;
        String body = String.format(
                "Dear %s,%n%n" +
                        "Welcome to Macota! We are delighted that you have chosen our platform for your pet care needs. " +
                        "At Macota, we strive to provide exceptional service and quality products to keep your furry friends happy and healthy.%n%n" +
                        "Here are a few things you can do now:%n" +
                        "1. Browse our <link>catalog of products</link> tailored to your pet's needs.%n" +
                        "2. Review our <link>care guides and tips</link> written by industry experts.%n" +
                        "3. Contact our support team anytime at support@macota.com for personalized assistance.%n%n" +
                        "We look forward to serving you. If you have any questions, feel free to reply to this email or reach us at +34 900 123 456.%n%n" +
                        "Sincerely,%n" +
                        "The Macota Team%n" +
                        "Macota S.L. | Calle Falsa 123, Sevilla, Spain%n" +
                        "" , name);
        sendEmail(email, subject, body);
    }

    /**
     * Sends a detailed order confirmation email.
     */
    public void notifyOrder(String email, double totalAmount, LocalDate date)
            throws DAOException, EntityNotFoundException, MessagingException {
        User user = userDAO.findByEmail(email);
        if (!(user instanceof Client)) {
            throw new EntityNotFoundException("User is not a client: " + email);
        }
        String name          = ((Client) user).getUserName();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String subject       = "Your Macota Order Confirmation";
        String body = String.format(
                "Hello %s,%n%n" +
                        "Thank you for your recent order with Macota. We appreciate your trust in us. Below are the details of your purchase:%n%n" +
                        "• Order Date: %s%n" +
                        "• Total Amount: €%.2f%n" +
                        "• Payment Method: card%n%n" +
                        "You can expect your order to be processed and shipped within 1-2 business days. We will send you another notification once your package is on its way.%n%n" +
                        "If you have any questions or need to make changes to your order, please reply to this email or contact our customer service at support@macota.com.%n%n" +
                        "Thank you for choosing Macota. We hope to see you again soon!%n%n" +
                        "Best regards,%n" +
                        "Macota Fulfillment Team%n" +
                        "Macota S.L. | Calle Falsa 123, Sevilla, Spain%n", name, formattedDate, totalAmount);
        sendEmail(email, subject, body);
    }

    /**
     * Internal helper to dispatch the SMTP message.
     */
    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(FROM_EMAIL));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }
}

