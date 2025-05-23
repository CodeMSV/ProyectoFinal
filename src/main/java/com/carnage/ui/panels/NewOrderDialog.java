// src/main/java/com/carnage/ui/panels/NewOrderDialog.java
package com.carnage.ui.panels;

import com.carnage.model.Product;
import com.carnage.model.Sale;
import com.carnage.model.user.client.Client;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.service.EmailNotificationService;
import com.carnage.service.ProductService;
import com.carnage.service.SaleService;
import jakarta.mail.MessagingException;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewOrderDialog extends JDialog {

    private final Client client;
    private final ProductService productService;
    private final SaleService saleService;
    private final EmailNotificationService emailService;
    private final Runnable onOrderCreated;

    private JTable productsTable;
    private DefaultTableModel tableModel;

    private static class Line {
        final Product product;
        final int qty;

        Line(Product p, int q) {
            product = p;
            qty = q;
        }
    }

    private final List<Line> lines = new ArrayList<>();

    public NewOrderDialog(
            Frame owner,
            Client client,
            ProductService productService,
            SaleService saleService,
            EmailNotificationService emailService,
            List<PaymentMethod> paymentMethods,
            Runnable onOrderCreated
    ) {
        super(owner, "Nuevo Pedido", true);
        this.client = client;
        this.productService = productService;
        this.saleService = saleService;
        this.emailService = emailService;
        this.onOrderCreated = onOrderCreated;

        initComponents(paymentMethods);
        loadProducts();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(List<PaymentMethod> paymentMethods) {
        setLayout(new BorderLayout(10, 10));

        String[] cols = {"ID", "Producto", "Precio", "Stock", "Cantidad"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                switch (col) {
                    case 0:
                        return Integer.class;
                    case 2:
                        return Double.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };
        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(24);
        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JComboBox<PaymentMethod> cbPayment = new JComboBox<>(
                paymentMethods.toArray(new PaymentMethod[0])
        );
        south.add(new JLabel("Método de pago:"));
        south.add(cbPayment);
        JButton btnCancel = new JButton("Cancelar");
        JButton btnConfirm = new JButton("Confirmar Pedido");
        south.add(btnCancel);
        south.add(btnConfirm);
        add(south, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnConfirm.addActionListener(e -> onConfirm((PaymentMethod) cbPayment.getSelectedItem()));
    }

    private void loadProducts() {
        try {
            for (Product p : productService.getAllProducts()) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getQuantityInStock(),
                        0
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando productos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onConfirm(PaymentMethod method) {
        lines.clear();
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = (Integer) tableModel.getValueAt(i, 4);
            if (qty > 0) {
                Product p = new Product(
                        (String) tableModel.getValueAt(i, 1),
                        (Double) tableModel.getValueAt(i, 2),
                        0, null, null
                );
                p.setId((Integer) tableModel.getValueAt(i, 0));
                lines.add(new Line(p, qty));
                total += p.getPrice() * qty;
            }
        }
        if (lines.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar al menos un producto con cantidad > 0",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Product> prods = new ArrayList<>();
        for (Line ln : lines)
            for (int c = 0; c < ln.qty; c++)
                prods.add(ln.product);

        Sale sale = new Sale(
                client.getId(),
                prods,
                total,
                LocalDate.now(),
                method
        );

        try {
            saleService.createSale(sale);
            try {
                emailService.notifyOrder(client.getUserEmail(), total, LocalDate.now());
                JOptionPane.showMessageDialog(this,
                        "Pedido creado con éxito (ID: " + sale.getId() + ").\n" +
                                "Te hemos enviado la confirmación por email.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (MessagingException me) {
                JOptionPane.showMessageDialog(this,
                        "Pedido creado, pero fallo enviando email: " + me.getMessage(),
                        "Atención", JOptionPane.WARNING_MESSAGE);
            }
            onOrderCreated.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear pedido: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
