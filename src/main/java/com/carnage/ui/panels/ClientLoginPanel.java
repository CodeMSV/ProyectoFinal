package com.carnage.ui.panels;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.user.client.Client;
import com.carnage.service.UserService;
import com.carnage.service.EmailNotificationService;


import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ClientLoginPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLink;
    private UserService userService;
    private EmailNotificationService emailService;


    public ClientLoginPanel(
            UserService userService,
            EmailNotificationService emailService,
            Runnable onRegisterClick,
            java.util.function.Consumer<Client> onLoginSuccess
    ) {
        this.userService = Objects.requireNonNull(userService);
        this.emailService = Objects.requireNonNull(emailService);

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        emailField = new JTextField();
        // TODO: add regex validation for email format
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField();
        add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginButton = new JButton("Entrar");
        add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        registerLink = new JLabel("<HTML><U>Si aún no te has registrado, pincha aquí</U></HTML>");
        registerLink.setForeground(Color.BLUE.darker());
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(registerLink, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            try {
                Client client = (Client) userService.authenticate(email, password);
                JOptionPane.showMessageDialog(this, "Login successful");
                // TODO: optionally send a notification email
                onLoginSuccess.accept(client);
            } catch (EntityNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Data access error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onRegisterClick.run();
            }
        });
    }
}
