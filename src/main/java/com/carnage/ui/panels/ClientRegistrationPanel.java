package com.carnage.ui.panels;

import com.carnage.model.user.client.Client;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.model.user.client.validation.*;
import com.carnage.service.UserService;
import com.carnage.service.EmailNotificationService;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.InvalidTextEnterException;
import jakarta.mail.MessagingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Panel para el registro de nuevos clientes.
 * Los campos del formulario se vacían cada vez que el panel se muestra.
 */
public class ClientRegistrationPanel extends JPanel {

    private final UserService userService;
    private final EmailNotificationService emailService;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField addressField;
    private JTextField phoneField;
    private JComboBox<PaymentMethod> paymentMethodCombo;
    private JButton registerButton;
    private JButton cancelButton;

    /**
     * Constructs the registration panel.
     * Fields are cleared whenever this panel becomes visible.
     */
    public ClientRegistrationPanel(
            UserService userService,
            EmailNotificationService emailService,
            List<PaymentMethod> paymentMethods,
            Consumer<Client> onSuccess,
            Runnable onCancel
    ) {
        this.userService  = Objects.requireNonNull(userService);
        this.emailService = Objects.requireNonNull(emailService);

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Initialize fields
        nameField      = new JTextField();
        surnameField   = new JTextField();
        emailField     = new JTextField();
        passwordField  = new JPasswordField();
        addressField   = new JTextField();
        phoneField     = new JTextField();
        paymentMethodCombo = new JComboBox<>(paymentMethods.toArray(new PaymentMethod[0]));

        // Add fields to layout
        addField("Nombre:",     nameField,      row++, gbc);
        addField("Apellidos:",  surnameField,   row++, gbc);
        addField("Email:",      emailField,     row++, gbc);
        addField("Contraseña:", passwordField,  row++, gbc);
        addField("Dirección:",  addressField,   row++, gbc);
        addField("Teléfono:",   phoneField,     row++, gbc);

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Método de Pago:"), gbc);
        gbc.gridx = 1;
        add(paymentMethodCombo, gbc);
        row++;

        // Buttons panel
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
        btnPanel.setBackground(Color.WHITE);
        registerButton = new JButton("Registrar");
        cancelButton   = new JButton("Volver al login");
        btnPanel.add(registerButton);
        btnPanel.add(cancelButton);
        add(btnPanel, gbc);

        // Clear fields when panel shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                clearFields();
            }
        });

        // Register button listener
        registerButton.addActionListener(e -> {
            try {
                NameValidator.validate(nameField.getText());
                SurnameValidator.validate(surnameField.getText());
                EmailValidator.validate(emailField.getText());
                PasswordValidator.validate(new String(passwordField.getPassword()));
                AddressValidator.validate(addressField.getText());
                PhoneValidator.validate(phoneField.getText());
                PaymentMethodValidator.validate((PaymentMethod) paymentMethodCombo.getSelectedItem());

                String fullName = nameField.getText().trim() + " " + surnameField.getText().trim();
                Client client = new Client(
                        fullName,
                        emailField.getText().trim(),
                        new String(passwordField.getPassword()),
                        addressField.getText().trim(),
                        phoneField.getText().trim()
                );

                client = userService.registerClient(client);
                try {
                    emailService.notifyRegistration(client.getUserEmail());
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this,
                        "Registro exitoso. ¡Bienvenido, " + fullName + "!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                onSuccess.accept(client);
            } catch (InvalidTextEnterException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            } catch (DAOException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    JOptionPane.showMessageDialog(this,
                            "Ya existe un usuario con ese email.",
                            "Error de registro", JOptionPane.WARNING_MESSAGE);
                } else {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error al registrar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> onCancel.run());
    }

    /**
     * Clears all input fields and resets the payment method selection.
     */
    private void clearFields() {
        nameField.setText("");
        surnameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        addressField.setText("");
        phoneField.setText("");
        paymentMethodCombo.setSelectedIndex(-1);
    }

    /** Helper to add label + field in the layout. */
    private void addField(String label, JComponent field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }
}
