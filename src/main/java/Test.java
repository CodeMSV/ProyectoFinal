import com.carnage.dao.invoiceDAO.InvoiceDAO;
import com.carnage.dao.invoiceDAO.JdbcInvoiceDAO;
import com.carnage.dao.saleDAO.JdbcSaleDAO;
import com.carnage.dao.saleDAO.SaleDAO;
import com.carnage.dao.supplierDAO.JdbcSupplierOrderDAO;
import com.carnage.dao.supplierDAO.JdbcSupplierOrderItemDAO;
import com.carnage.dao.supplierDAO.SupplierOrderDAO;
import com.carnage.dao.supplierDAO.SupplierOrderItemDAO;
import com.carnage.dao.userDAO.JdbcPaymentMethodDAO;
import com.carnage.dao.userDAO.JdbcUserDAO;
import com.carnage.dao.userDAO.PaymentMethodDAO;
import com.carnage.dao.userDAO.UserDAO;
import com.carnage.service.*;
import com.carnage.ui.MainFrame;
import com.carnage.util.DDBB.ConnectionDDBB;

import javax.swing.*;

public class Test {

    public static void main(String[] args) {
        // Initialize JDBC connection manager
        ConnectionDDBB connectionManager = new ConnectionDDBB();

        // Instantiate DAOs (JDBC implementations)
        UserDAO userDAO = new JdbcUserDAO(connectionManager);
        PaymentMethodDAO paymentMethodDAO = new JdbcPaymentMethodDAO();
        SaleDAO saleDAO = new JdbcSaleDAO(connectionManager);
        InvoiceDAO invoiceDAO = new JdbcInvoiceDAO();
        SupplierOrderDAO orderDAO = new JdbcSupplierOrderDAO(connectionManager);
        SupplierOrderItemDAO itemDAO = new JdbcSupplierOrderItemDAO(connectionManager);

        // Instantiate services
        UserService userService = new UserService(userDAO);
        EmailNotificationService emailService = new EmailNotificationService(userDAO);
        PaymentMethodService paymentMethodService = new PaymentMethodService(paymentMethodDAO);
        SaleService saleService = new SaleService(saleDAO);
        InvoiceService invoiceService = new InvoiceService(invoiceDAO);
        SupplierOrderService supplierOrderService = new SupplierOrderService(orderDAO, itemDAO);

        // Launch Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(
                    userService,
                    emailService,
                    paymentMethodService,
                    saleService,
                    invoiceService,
                    supplierOrderService
            );
            frame.setVisible(true);
        });
    }
}
