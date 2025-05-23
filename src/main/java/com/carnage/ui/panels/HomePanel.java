package com.carnage.ui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HomePanel extends JPanel {

    private JButton clientAccessButton;
    private JButton adminAccessButton;
    private JLabel titleLabel;
    private BufferedImage backgroundImage;

    public HomePanel(Runnable onClientClick, Runnable onAdminClick) {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen de fondo: "+ e.getMessage());
        }

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setOpaque(false); // transparente para ver fondo
        clientAccessButton = new JButton("Acceso Cliente");
        adminAccessButton  = new JButton("Acceso Administrador");
        clientAccessButton.setPreferredSize(new Dimension(200, 50));
        adminAccessButton .setPreferredSize(new Dimension(200, 50));
        clientAccessButton.addActionListener(e -> onClientClick.run());
        adminAccessButton .addActionListener(e -> onAdminClick.run());
        buttonPanel.add(clientAccessButton);
        buttonPanel.add(adminAccessButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
