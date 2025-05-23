package com.carnage.ui;

import com.carnage.dao.productDAO.JdbcProductDAO;
import com.carnage.dao.saleDAO.JdbcSaleDAO;
import com.carnage.dao.invoiceDAO.JdbcInvoiceDAO;
import com.carnage.dao.supplierDAO.JdbcSupplierOrderDAO;
import com.carnage.dao.supplierDAO.JdbcSupplierOrderItemDAO;
import com.carnage.dao.userDAO.JdbcPaymentMethodDAO;
import com.carnage.dao.userDAO.JdbcUserDAO;
import com.carnage.model.user.admin.Admin;
import com.carnage.model.user.client.Client;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.service.EmailNotificationService;
import com.carnage.service.InvoiceService;
import com.carnage.service.PaymentMethodService;
import com.carnage.service.ProductService;
import com.carnage.service.SaleService;
import com.carnage.service.SupplierOrderService;
import com.carnage.service.UserService;
import com.carnage.ui.panels.AdminDashboardPanel;
import com.carnage.ui.panels.AdminLoginPanel;
import com.carnage.ui.panels.ClientDashboardPanel;
import com.carnage.ui.panels.ClientLoginPanel;
import com.carnage.ui.panels.ClientRegistrationPanel;
import com.carnage.ui.panels.HomePanel;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final UserService userService;
    private final EmailNotificationService emailService;
    private final PaymentMethodService paymentMethodService;
    private final ProductService productService;
    private final SaleService saleService;
    private final InvoiceService invoiceService;
    private final SupplierOrderService supplierOrderService;

    public MainFrame(
            UserService userService,
            EmailNotificationService emailService,
            PaymentMethodService paymentMethodService,
            SaleService saleService,
            InvoiceService invoiceService,
            SupplierOrderService supplierOrderService
    ) {
        super("Aplicación Carnage");
        this.userService = userService;
        this.emailService = emailService;
        this.paymentMethodService = paymentMethodService;
        this.productService = new ProductService(new JdbcProductDAO(new ConnectionDDBB()));
        this.saleService = saleService;
        this.invoiceService = invoiceService;
        this.supplierOrderService = supplierOrderService;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setResizable(false);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        setContentPane(contentPanel);

        try {
            initPanels();
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error iniciando panels: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initPanels() throws DAOException {
        contentPanel.add(new HomePanel(
                () -> showPanel("ClientLogin"),
                () -> showPanel("AdminLogin")
        ), "Home");

        contentPanel.add(new ClientLoginPanel(
                userService,
                emailService,
                () -> showPanel("ClientRegistration"),
                this::onClientLoginSuccess
        ), "ClientLogin");

        List<PaymentMethod> methods;
        try {
            methods = paymentMethodService.listAll();
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando métodos de pago: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            methods = Collections.emptyList();
        }
        contentPanel.add(new ClientRegistrationPanel(
                userService,
                emailService,
                methods,
                this::onClientLoginSuccess,
                () -> showPanel("ClientLogin")
        ), "ClientRegistration");

        contentPanel.add(new AdminLoginPanel(
                userService,
                this::onAdminLoginSuccess
        ), "AdminLogin");

        contentPanel.add(new AdminDashboardPanel(
                userService,
                saleService,
                productService,
                supplierOrderService,
                () -> showPanel("Home")
        ), "AdminDashboard");

        // Show home
        showPanel("Home");
    }

    private void onClientLoginSuccess(Client client) {
        ClientDashboardPanel dashboard = new ClientDashboardPanel(
                client,
                productService,
                saleService,
                invoiceService,
                paymentMethodService,
                emailService,             // ← Asegúrate de incluirlo
                () -> showPanel("Home")
        );
        contentPanel.add(dashboard, "ClientDashboard");
        showPanel("ClientDashboard");
    }

    private void onAdminLoginSuccess(Admin admin) {
        showPanel("AdminDashboard");
    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        });
    }
}