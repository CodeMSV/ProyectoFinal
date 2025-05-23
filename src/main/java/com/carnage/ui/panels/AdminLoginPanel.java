// src/main/java/com/carnage/ui/panels/AdminLoginPanel.java
package com.carnage.ui.panels;

import com.carnage.model.user.admin.Admin;
import com.carnage.service.UserService;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * Panel for administrator login with improved styling.
 */
public class AdminLoginPanel extends JPanel {

    private final UserService userService;
    private final Consumer<Admin> onSuccess;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public AdminLoginPanel(UserService userService, Consumer<Admin> onSuccess) {
        this.userService = userService;
        this.onSuccess = onSuccess;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Admin Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // Email label
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblEmail, gbc);

        // Email field
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1;
        add(txtEmail, gbc);

        // Password label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);

        // Password field
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.BLACK);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 10, 20);
        add(btnLogin, gbc);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Admin admin = (Admin) userService.authenticate(
                            txtEmail.getText().trim(),
                            new String(txtPassword.getPassword())
                    );
                    onSuccess.accept(admin);
                } catch (DAOException ex) {
                    JOptionPane.showMessageDialog(
                            AdminLoginPanel.this,
                            "Login failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}