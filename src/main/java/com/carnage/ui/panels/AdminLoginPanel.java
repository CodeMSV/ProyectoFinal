package com.carnage.ui.panels;

import com.carnage.model.user.admin.Admin;
import com.carnage.service.UserService;
import com.carnage.util.dao.DAOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

public class AdminLoginPanel extends JPanel {

    private final UserService userService;
    private final Consumer<Admin> onSuccess;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private BufferedImage backgroundImage;

    public AdminLoginPanel(UserService userService, Consumer<Admin> onSuccess) {
        this.userService = userService;
        this.onSuccess = onSuccess;
        loadBackground();
        initComponents();
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font titleFont   = new Font("SansSerif", Font.BOLD, 32);
        Font labelFont   = new Font("SansSerif", Font.PLAIN, 14);
        Font fieldFont   = new Font("SansSerif", Font.PLAIN, 14);
        Font buttonFont  = new Font("SansSerif", Font.BOLD, 14);
        Color labelColor = new Color(60, 60, 60);
        Color buttonBg   = new Color(0, 120, 215);
        Color buttonFg   = Color.BLACK;


        // Email label
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblEmail, gbc);

        // Email field
        txtEmail = new JTextField(20);
        txtEmail.setFont(fieldFont);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        // Password label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(labelFont);
        lblPassword.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);

        // Password field
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(fieldFont);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Login button (menos ancho, más alto)
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(buttonFont);
        btnLogin.setBackground(buttonBg);
        btnLogin.setForeground(buttonFg);
        btnLogin.setPreferredSize(new Dimension(120, 40)); // ancho 120px, alto 40px
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;            // para respetar el preferredSize
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

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}
