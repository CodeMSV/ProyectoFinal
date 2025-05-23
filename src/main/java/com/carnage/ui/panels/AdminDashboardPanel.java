// src/main/java/com/carnage/ui/panels/AdminDashboardPanel.java
package com.carnage.ui.panels;

import com.carnage.model.Product;
import com.carnage.model.user.client.Client;
import com.carnage.model.supplier.SupplierOrder;
import com.carnage.model.user.admin.Admin;
import com.carnage.service.ProductService;
import com.carnage.service.SaleService;
import com.carnage.service.SupplierOrderService;
import com.carnage.service.UserService;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AdminDashboardPanel extends JPanel {

    private final UserService userService;
    private final SaleService saleService;
    private final ProductService productService;
    private final SupplierOrderService supplierOrderService;
    private final Runnable onLogout;

    private JTabbedPane tabbedPane;
    private JTable usersTable;
    private JTable userOrdersTable;
    private JTable supplierOrdersTable;
    private JTable productsTable;

    public AdminDashboardPanel(
            UserService userService,
            SaleService saleService,
            ProductService productService,
            SupplierOrderService supplierOrderService,
            Runnable onLogout
    ) {
        this.userService = userService;
        this.saleService = saleService;
        this.productService = productService;
        this.supplierOrderService = supplierOrderService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        add(createTopBar(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Cerrar sesión");
        btnLogout.addActionListener(e -> onLogout.run());
        bar.add(btnLogout);
        return bar;
    }

    private JTabbedPane createTabs() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Usuarios", createUsersPanel());
        tabbedPane.addTab("Pedidos Usuarios", createUserOrdersPanel());
        tabbedPane.addTab("Pedido Proveedor", createSupplierOrdersPanel());
        tabbedPane.addTab("Productos", createProductsPanel());

        return tabbedPane;
    }

    private JPanel createUsersPanel() {
        JPanel userCreatePanel = new JPanel(new BorderLayout());

        usersTable = new JTable(createTableModel(new String[]{"ID", "Nombre", "Email"}));
        userCreatePanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        JButton btn = new JButton("Actualizar");

        btn.addActionListener(e -> loadUsers());
        userCreatePanel.add(btn, BorderLayout.SOUTH);
        loadUsers();

        return userCreatePanel;
    }

    private void loadUsers() {
        DefaultTableModel tableModelUSer = (DefaultTableModel) usersTable.getModel();
        tableModelUSer.setRowCount(0);
        try {
            List<Client> clients = userService.listAllClients();
            for (Client c : clients) {
                tableModelUSer.addRow(new Object[]{c.getId(), c.getUserName(), c.getUserEmail()});
            }
        } catch (DAOException ex) {
            showError("Error cargando usuarios: " + ex.getMessage());
        }
    }

    private JPanel createUserOrdersPanel() {
        JPanel userOrderPanel = new JPanel(new BorderLayout());

        userOrdersTable = new JTable(createTableModel(new String[]{"Usuario", "# Pedidos"}));
        userOrderPanel.add(new JScrollPane(userOrdersTable), BorderLayout.CENTER);

        JButton btn = new JButton("Actualizar");

        btn.addActionListener(e -> loadUserOrders());
        userOrderPanel.add(btn, BorderLayout.SOUTH);
        loadUserOrders();

        return userOrderPanel;
    }

    private void loadUserOrders() {
        DefaultTableModel tableModel = (DefaultTableModel) userOrdersTable.getModel();
        tableModel.setRowCount(0);
        try {
            for (Client c : userService.listAllClients()) {
                int count = saleService.countSalesByClient(c.getId());
                tableModel.addRow(new Object[]{c.getUserName(), count});
            }
        } catch (DAOException ex) {
            showError("Error cargando pedidos: " + ex.getMessage());
        }
    }

    private JPanel createSupplierOrdersPanel() {
        JPanel supplierOrderPanel = new JPanel(new BorderLayout());

        supplierOrdersTable = new JTable(createTableModel(new String[]{"ID", "Fecha", "Estado"}));
        supplierOrderPanel.add(new JScrollPane(supplierOrdersTable), BorderLayout.CENTER);

        JButton btn = new JButton("Nuevo Pedido Proveedor");

        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad no implementada.");
        });
        supplierOrderPanel.add(btn, BorderLayout.SOUTH);
        loadSupplierOrders();

        return supplierOrderPanel;
    }


    private void loadSupplierOrders() {
        DefaultTableModel supplierTable = (DefaultTableModel) supplierOrdersTable.getModel();
        supplierTable.setRowCount(0);

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (SupplierOrder so : supplierOrderService.getAllOrders()) {
                supplierTable.addRow(new Object[]{so.getId(), so.getCreatedAt().format(fmt), so.getStatus()});
            }

        } catch (DAOException ex) {
            showError("Error pedidos proveedor: " + ex.getMessage());
        }
    }

    private JPanel createProductsPanel() {
        JPanel createProductPanel = new JPanel(new BorderLayout());

        productsTable = new JTable(createTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}));
        createProductPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Añadir");
        JButton btnEdit = new JButton("Modificar");
        JButton btnDelete = new JButton("Eliminar");

        backPanel.add(btnAdd);
        backPanel.add(btnEdit);
        backPanel.add(btnDelete);
        createProductPanel.add(backPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> openProductDialog(null));
        btnEdit.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            if (row >= 0) openProductDialog((Integer) productsTable.getValueAt(row, 0));
        });

        btnDelete.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            if (row >= 0) {
                Integer id = (Integer) productsTable.getValueAt(row, 0);
                try {
                    productService.deleteProduct(id);
                    loadProducts();
                } catch (DAOException ex) {
                    showError("Error eliminando: " + ex.getMessage());
                }
            }
        });

        loadProducts();

        return createProductPanel;
    }

    private void loadProducts() {
        DefaultTableModel productsTableLoad = (DefaultTableModel) productsTable.getModel();
        productsTableLoad.setRowCount(0);

        try {
            for (Product product : productService.getAllProducts()) {
                productsTableLoad.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getQuantityInStock()});
            }

        } catch (DAOException ex) {
            showError("Error cargando productos: " + ex.getMessage());
        }
    }

    private void openProductDialog(Integer productId) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        ProductDialog dlg = new ProductDialog(owner, productService, productId, this::loadProducts);
        dlg.setVisible(true);
    }

    private DefaultTableModel createTableModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
    }


    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
