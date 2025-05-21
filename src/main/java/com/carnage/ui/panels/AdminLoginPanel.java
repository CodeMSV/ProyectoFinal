package com.carnage.ui.panels;

import com.carnage.model.user.admin.Admin;
import com.carnage.service.UserService;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import javax.swing.*;
import java.awt.*;

public class AdminLoginPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;

    /**
     * Constructs the admin login panel.
     *
     * @param userService    service for authentication
     * @param onLoginSuccess callback when login succeeds, receives the authenticated Admin
     */
    public AdminLoginPanel(
            UserService userService,
            java.util.function.Consumer<Admin> onLoginSuccess
    ) {
        this.userService = userService;

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email label and field
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        add(emailField, gbc);

        // Password label and field
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        add(passwordField, gbc);

        // Login button
        gbc.gridx = 1; gbc.gridy = 2;
        loginButton = new JButton("Entrar");
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            try {
                Admin admin = (Admin) userService.authenticate(email, password);
                JOptionPane.showMessageDialog(this, "Login successful");
                onLoginSuccess.accept(admin);
            } catch (EntityNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Data access error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
