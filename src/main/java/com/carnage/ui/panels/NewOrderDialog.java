package com.carnage.ui.panels;

import com.carnage.model.Product;
import com.carnage.model.Sale;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.service.ProductService;
import com.carnage.service.SaleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewOrderDialog extends JDialog {
    private final ProductService productService;
    private final SaleService saleService;
    private final int clientId;
    private final Runnable onOrderCreated;

    private JTable productsTable;
    private DefaultTableModel tableModel;
    private JComboBox<PaymentMethod> cbPayment;
    private JButton btnConfirm, btnCancel;

    // Clase interna para línea de pedido
    private static class Line {
        final Product product;
        final int qty;
        Line(Product p, int q) { product = p; qty = q; }
    }
    private final List<Line> lines = new ArrayList<>();

    public NewOrderDialog(Frame owner,
                          int clientId,
                          ProductService productService,
                          SaleService saleService,
                          List<PaymentMethod> paymentMethods,
                          Runnable onOrderCreated) {
        super(owner, "Nuevo Pedido", true);
        this.clientId = clientId;
        this.productService = productService;
        this.saleService = saleService;
        this.onOrderCreated = onOrderCreated;

        initComponents(paymentMethods);
        loadProducts();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(List<PaymentMethod> paymentMethods) {
        setLayout(new BorderLayout(10,10));

        // Definimos columnas con tipos: ID=Integer, Producto=String, Precio=Double, Stock=Integer, Cantidad=Integer
        String[] cols = {"ID","Producto","Precio","Stock","Cantidad"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                // Sólo la columna 4 (Cantidad) editable
                return c == 4;
            }
            @Override public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 2: return Double.class;
                    case 3: return Integer.class;
                    case 4: return Integer.class;
                    default: return String.class;
                }
            }
        };
        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(24);
        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        // Panel inferior
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        cbPayment = new JComboBox<>(paymentMethods.toArray(new PaymentMethod[0]));
        south.add(new JLabel("Método de pago:"));
        south.add(cbPayment);
        btnConfirm = new JButton("Confirmar Pedido");
        btnCancel  = new JButton("Cancelar");
        south.add(btnCancel);
        south.add(btnConfirm);
        add(south, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnConfirm.addActionListener(e -> onConfirm());
    }

    private void loadProducts() {
        try {
            for (Product p : productService.getAllProducts()) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getPrice(),           // doble sin formatear
                        p.getQuantityInStock(),
                        0                       // cantidad inicial
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando productos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onConfirm() {
        lines.clear();
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = (Integer) tableModel.getValueAt(i, 4);
            if (qty > 0) {
                int prodId        = (Integer) tableModel.getValueAt(i, 0);
                String name       = (String)  tableModel.getValueAt(i, 1);
                double price      = (Double)  tableModel.getValueAt(i, 2);  // recuperamos Double
                Product p = new Product(name, price, 0, null, null);
                p.setId(prodId);
                lines.add(new Line(p, qty));
                total += price * qty;
            }
        }
        if (lines.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar al menos un producto con cantidad > 0",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Construimos la venta
        List<Product> prods = new ArrayList<>();
        for (Line ln : lines) {
            for (int c=0; c<ln.qty; c++) prods.add(ln.product);
        }
        Sale sale = new Sale(
                clientId,
                prods,
                total,
                LocalDate.now(),
                (PaymentMethod) cbPayment.getSelectedItem()
        );

        // Persistimos
        try {
            saleService.createSale(sale);
            JOptionPane.showMessageDialog(this,
                    "Pedido creado con éxito (ID: " + sale.getId() + ")",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            onOrderCreated.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear pedido: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
