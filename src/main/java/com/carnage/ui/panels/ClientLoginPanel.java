package com.carnage.ui.panels;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.user.client.Client;
import com.carnage.service.UserService;
import com.carnage.service.EmailNotificationService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ClientLoginPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLink;
    private UserService userService;
    private EmailNotificationService emailService;
    private BufferedImage backgroundImage;

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
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        emailField = new JTextField();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
