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

public class EmailNotificationService {

    private final UserDAO userDAO;
    private final Session mailSession;

    private static final String GMAIL_USER = "migue.626@gmail.com";
    private static final String GMAIL_APP_PASSWORD = "brss yken fjiu esjp".replace(" ", "");
    private static final String FROM_EMAIL = GMAIL_USER;


    /**
     * Constructor for EmailNotificationService.
     * Initializes the mail session with Gmail SMTP settings.
     *
     * @param userDAO The UserDAO instance to interact with user data.
     */
    public EmailNotificationService(UserDAO userDAO) {
        this.userDAO = userDAO;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        this.mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, GMAIL_APP_PASSWORD);
            }
        });
    }


    /**
     * Sends a welcome email to the user upon registration.
     *
     * @param email The email address of the user.
     * @throws DAOException            If there is an error accessing the database.
     * @throws EntityNotFoundException If the user is not found in the database.
     * @throws MessagingException      If there is an error sending the email.
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
                        "Welcome to Macota Butcher Shop! We are delighted that you have chosen our shop for your fresh meat needs. " +
                        "At Macota Butcher Shop, we strive to offer the finest cuts and the highest quality.%n%n" +
                        "Here’s what you can do now:%n" +
                        "1. Browse our <link>meat selection catalog</link>, carefully chosen for your satisfaction.%n" +
                        "2. Check out our <link>recipes and preparation tips</link> crafted by master butchers.%n" +
                        "3. Contact our team at +34 900 123 456 or email us at hello@macota.com for personalized assistance.%n%n" +
                        "We look forward to serving you. If you have any questions, just reply to this email or visit us at Calle Falsa 123, Seville, Spain.%n%n" +
                        "Sincerely,%n" +
                        "The Macota Butcher Shop Team%n" +
                        "Macota Butcher Shop S.L. | Calle Falsa 123, Seville, Spain%n" +
                        "", name);
        sendEmail(email, subject, body);
    }


    /**
     * Sends an order confirmation email to the user.
     *
     * @param email       The email address of the user.
     * @param totalAmount The total amount of the order.
     * @param date        The date of the order.
     * @throws DAOException            If there is an error accessing the database.
     * @throws EntityNotFoundException If the user is not found in the database.
     * @throws MessagingException      If there is an error sending the email.
     */
    public void notifyOrder(String email, double totalAmount, LocalDate date)
            throws DAOException, EntityNotFoundException, MessagingException {
        User user = userDAO.findByEmail(email);
        if (!(user instanceof Client)) {
            throw new EntityNotFoundException("User is not a client: " + email);
        }
        String name = ((Client) user).getUserName();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String subject = "Your Macota Order Confirmation";
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
     * Sends a password reset email to the user.
     *
     * @param email The email address of the user.
     * @throws DAOException            If there is an error accessing the database.
     * @throws EntityNotFoundException If the user is not found in the database.
     * @throws MessagingException      If there is an error sending the email.
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

