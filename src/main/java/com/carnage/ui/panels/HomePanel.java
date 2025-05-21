package com.carnage.ui.panels;

import javax.swing.*;
import java.awt.*;

import static com.sun.javafx.fxml.expression.Expression.add;
import static javax.swing.text.StyleConstants.setBackground;

public class HomePanel  extends JPanel {

    private JButton clientAccessButton;
    private JButton adminAccessButton;
    private JLabel logoLabel;
    private JLabel titleLabel;

    /**
     * Constructs the home panel.
     *
     * @param onClientClick callback invoked when "Acceso Cliente" is clicked
     * @param onAdminClick  callback invoked when "Acceso Administrador" is clicked
     */
    public HomePanel(Runnable onClientClick, Runnable onAdminClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        // Logo placeholder (replace with actual resource path)
        logoLabel = new JLabel("Logo Placeholder", SwingConstants.CENTER);
        // TODO: Replace with: new ImageIcon(getClass().getResource("/resources/logo.png"))
        add(logoLabel, BorderLayout.NORTH);

        // Company name title
        titleLabel = new JLabel("Macota", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        add(titleLabel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(Color.WHITE);

        clientAccessButton = new JButton("Acceso Cliente");
        adminAccessButton = new JButton("Acceso Administrador");

        clientAccessButton.setPreferredSize(new Dimension(200, 50));
        adminAccessButton.setPreferredSize(new Dimension(200, 50));

        clientAccessButton.addActionListener(e -> onClientClick.run());
        adminAccessButton.addActionListener(e -> onAdminClick.run());

        buttonPanel.add(clientAccessButton);
        buttonPanel.add(adminAccessButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
