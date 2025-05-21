package com.carnage.ui.panels;

import com.carnage.model.supplier.SupplierOrder;
import com.carnage.service.SupplierOrderService;
import com.carnage.util.dao.DAOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel to display supplier orders in a table format.
 * <p>
 * Utilizes Swing components to render a list of orders
 * and handles data retrieval exceptions appropriately.
 */
public class AdminDashboardPanel extends JPanel {

    private final SupplierOrderService orderService;
    private final Runnable onLogout;
    private JTable ordersTable;

    /**
     * Creates a new AdminDashboardPanel.
     *
     * @param orderService service for fetching supplier orders
     * @param onLogout     callback to execute on logout action
     */
    public AdminDashboardPanel(SupplierOrderService orderService, Runnable onLogout) {
        this.orderService = orderService;
        this.onLogout = onLogout;
        initComponents();
        loadOrders();
    }

    /**
     * Initializes UI components and layout.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        ordersTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> onLogout.run());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(logoutBtn);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Retrieves supplier orders and populates the table model.
     * Handles DAOException by showing an error dialog.
     */
    private void loadOrders() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use the correct service method name
                List<SupplierOrder> orders = orderService.getAllOrders();

                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"Order ID", "Order Date", "Status"}, 0
                );

                for (SupplierOrder order : orders) {
                    model.addRow(new Object[]{
                            order.getId(),
                            order.getCreatedAt(),
                            order.getStatus()
                    });
                }
                ordersTable.setModel(model);

            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error loading orders: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
