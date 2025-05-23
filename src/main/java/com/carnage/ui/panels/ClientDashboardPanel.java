// src/main/java/com/carnage/ui/panels/ClientDashboardPanel.java
package com.carnage.ui.panels;

import com.carnage.model.Sale;
import com.carnage.model.user.client.Client;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.service.EmailNotificationService;
import com.carnage.service.InvoiceService;
import com.carnage.service.ProductService;
import com.carnage.service.SaleService;
import com.carnage.service.PaymentMethodService;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientDashboardPanel extends JPanel {

    private final Client currentClient;
    private final ProductService productService;
    private final SaleService saleService;
    private final InvoiceService invoiceService;
    private final PaymentMethodService paymentMethodService;
    private final EmailNotificationService emailService;
    private final Runnable onLogout;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel datosPanel = new JPanel(new GridBagLayout());
    private final JTable historyTable;
    private List<Sale> salesList;

    public ClientDashboardPanel(
            Client currentClient,
            ProductService productService,
            SaleService saleService,
            InvoiceService invoiceService,
            PaymentMethodService paymentMethodService,
            EmailNotificationService emailService,
            Runnable onLogout
    ) {
        this.currentClient = currentClient;
        this.productService = productService;
        this.saleService = saleService;
        this.invoiceService = invoiceService;
        this.paymentMethodService = paymentMethodService;
        this.emailService = emailService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topBar.setBackground(Color.WHITE);
        JButton btnDatos = new JButton("Datos Cliente");
        JButton btnHistorial = new JButton("Historial Pedidos");
        JButton btnNuevo = new JButton("Nuevo Pedido");
        JButton btnCerrar = new JButton("Cerrar sesión");
        topBar.add(btnDatos);
        topBar.add(btnHistorial);
        topBar.add(btnNuevo);
        topBar.add(btnCerrar);
        add(topBar, BorderLayout.NORTH);

        datosPanel.setBackground(Color.WHITE);
        datosPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Información del Cliente",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));
        cards.add(datosPanel, "DATOS");

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Historial de Pedidos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));

        String[] cols = {"Fecha", "Total (€)", "Estado Factura"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        historyTable = new JTable(model);
        historyTable.setRowHeight(24);
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        cards.add(historyPanel, "HISTORIAL");

        cards.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(cards, BorderLayout.CENTER);

        btnDatos.addActionListener(e -> showDatos());
        btnHistorial.addActionListener(e -> showHistorial());
        btnCerrar.addActionListener(e -> onLogout.run());
        btnNuevo.addActionListener(e -> {
            try {
                List<PaymentMethod> methods = paymentMethodService.listAll();
                NewOrderDialog dialog = new NewOrderDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        currentClient,
                        productService,
                        saleService,
                        emailService,
                        methods,
                        this::showHistorial
                );
                dialog.setVisible(true);
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error cargando métodos de pago: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && historyTable.getSelectedRow() >= 0) {
                    int row = historyTable.getSelectedRow();
                    Sale summary = salesList.get(row);
                    try {
                        Sale full = saleService.getSaleById(summary.getId());
                        System.out.println("DEBUG: Productos cargados = " + full.getProducts().size());
                        showOrderDetails(full);
                    } catch (DAOException ex) {
                        showOrderDetails(summary);
                    }
                }
            }
        });

        showDatos();
    }

    private void showDatos() {
        datosPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        String[][] info = {
                {"Nombre:", currentClient.getUserName()},
                {"Email:", currentClient.getUserEmail()},
                {"Teléfono:", currentClient.getPhone()},
                {"Dirección:", currentClient.getAddress()}
        };
        for (int i = 0; i < info.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            JLabel key = new JLabel(info[i][0]);
            key.setFont(new Font("SansSerif", Font.PLAIN, 12));
            datosPanel.add(key, gbc);
            gbc.gridx = 1;
            JLabel val = new JLabel(info[i][1]);
            val.setFont(new Font("SansSerif", Font.PLAIN, 12));
            datosPanel.add(val, gbc);
        }
        datosPanel.revalidate();
        datosPanel.repaint();
        cardLayout.show(cards, "DATOS");
    }

    private void showHistorial() {
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            salesList = saleService.getSalesByClient(currentClient.getId());
            for (Sale sale : salesList) {
                String estado = "Sin factura";
                try {
                    Optional<?> invOpt = invoiceService.getInvoiceByOrderId(sale.getId());
                    if (invOpt.isPresent()) estado = "Facturada";
                } catch (DAOException ignored) {
                }
                model.addRow(new Object[]{
                        sale.getDate().format(fmt),
                        String.format("%.2f", sale.getTotalPrice()),
                        estado
                });
            }
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando historial: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        cardLayout.show(cards, "HISTORIAL");
    }

    private void showOrderDetails(Sale sale) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder html = new StringBuilder("<html><body style='font-family:SansSerif;font-size:12px;'>");
        html.append("<h2>Detalle del Pedido</h2>");
        html.append(String.format("<p><b>ID Pedido:</b> %d<br>", sale.getId()));
        html.append(String.format("<b>Fecha:</b> %s<br>", sale.getDate().format(fmt)));
        html.append(String.format("<b>Total:</b> €%.2f<br>", sale.getTotalPrice()));
        PaymentMethod pm = sale.getPaymentMethod();
        html.append(String.format("<b>Método de pago:</b> %s</p>", pm));

        Map<String, Integer> counts = new HashMap<>();
        for (var p : sale.getProducts()) {
            counts.put(p.getName(), counts.getOrDefault(p.getName(), 0) + 1);
        }
        html.append("<p><b>Productos:</b></p><ul>");
        if (counts.isEmpty()) {
            html.append("<li><i>(ninguno)</i></li>");
        } else {
            for (var e : counts.entrySet()) {
                html.append(String.format("<li>%s x%d</li>", e.getKey(), e.getValue()));
            }
        }
        html.append("</ul></body></html>");

        JLabel content = new JLabel(html.toString());
        JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(350, 200));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Detalle del Pedido",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
