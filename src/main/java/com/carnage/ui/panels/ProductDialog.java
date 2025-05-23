package com.carnage.ui.panels;

import com.carnage.model.Product;
import com.carnage.model.product.ProductCategory;
import com.carnage.service.ProductService;
import com.carnage.util.dao.DAOException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;


public class ProductDialog extends JDialog {
    private final ProductService productService;
    private final Integer productId; // null for new
    private final Runnable onSuccess;

    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JComboBox<ProductCategory> cmbCategory;
    private JTextField txtExpiration;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductDialog(Frame owner,
                         ProductService productService,
                         Integer productId,
                         Runnable onSuccess) {
        super(owner, true);
        this.productService = productService;
        this.productId = productId;
        this.onSuccess = onSuccess;
        setTitle(productId == null ? "Add Product" : "Edit Product");
        initComponents();
        if (productId != null) loadProductData();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(10);
        panel.add(txtPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(10);
        panel.add(txtStock, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        cmbCategory = new JComboBox<>(ProductCategory.values());
        panel.add(cmbCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Expiration (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        txtExpiration = new JTextField(10);
        panel.add(txtExpiration, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton(productId == null ? "Add" : "Save");
        JButton btnCancel = new JButton("Cancel");
        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadProductData() {
        try {
            Product p = productService.getAllProducts().stream()
                    .filter(prod -> Objects.equals(prod.getId(), productId))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Product not found"));
            txtName.setText(p.getName());
            txtPrice.setText(String.valueOf(p.getPrice()));
            txtStock.setText(String.valueOf(p.getQuantityInStock()));
            cmbCategory.setSelectedItem(p.getCategory());
            txtExpiration.setText(p.getExpirationDate().format(DATE_FORMAT));
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onSave() {
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String stockText = txtStock.getText().trim();
        ProductCategory category = (ProductCategory) cmbCategory.getSelectedItem();
        String expText = txtExpiration.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || expText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        int stock;
        LocalDate expiration;
        try {
            price = Double.parseDouble(priceText);
            stock = Integer.parseInt(stockText);
            expiration = LocalDate.parse(expText, DATE_FORMAT);
        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number or date format.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Product product;
            if (productId == null) {
                product = new Product(name, price, stock, category, expiration);
                productService.createProduct(product);
            } else {
                product = new Product(name, price, stock, category, expiration);
                product.setId(productId);
                productService.updateProduct(product);
            }
            onSuccess.run();
            dispose();
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}