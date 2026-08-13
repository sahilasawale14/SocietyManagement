package admin;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public PaymentPage() {

        setTitle("Payments");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Payments");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Payment");
        addButton.addActionListener(e -> openAddPaymentForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editPayment());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deletePayment());

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(buttons, BorderLayout.EAST);

        // Search
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(
                e -> searchPayments(searchField.getText())
        );

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(top, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "ID",
                "Maintenance ID",
                "Payment Date",
                "Payment Mode",
                "Amount Paid",
                "Payment Status"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadPayments();

        setVisible(true);
    }

    // LOAD PAYMENTS
    private void loadPayments() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM PAYMENT";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("payment_id"),
                        rs.getInt("maintenance_id"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_mode"),
                        rs.getBigDecimal("amount_paid"),
                        rs.getString("payment_status")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    // ADD PAYMENT
    private void openAddPaymentForm() {

        JTextField maintenanceIdField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField modeField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField statusField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Maintenance ID:"));
        panel.add(maintenanceIdField);

        panel.add(new JLabel("Payment Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Payment Mode:"));
        panel.add(modeField);

        panel.add(new JLabel("Amount Paid:"));
        panel.add(amountField);

        panel.add(new JLabel("Payment Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Payment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO PAYMENT " +
                        "(maintenance_id, payment_date, payment_mode, " +
                        "amount_paid, payment_status) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(maintenanceIdField.getText()));
                ps.setString(2, dateField.getText());
                ps.setString(3, modeField.getText());
                ps.setBigDecimal(4,
                        new java.math.BigDecimal(amountField.getText()));
                ps.setString(5, statusField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Payment added successfully!"
                );

                model.setRowCount(0);
                loadPayments();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // EDIT PAYMENT
    private void editPayment() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payment first."
            );

            return;
        }

        int paymentId = (int) model.getValueAt(row, 0);

        JTextField maintenanceIdField =
                new JTextField(model.getValueAt(row, 1).toString());

        JTextField dateField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField modeField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField amountField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField statusField =
                new JTextField(model.getValueAt(row, 5).toString());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Maintenance ID:"));
        panel.add(maintenanceIdField);

        panel.add(new JLabel("Payment Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Payment Mode:"));
        panel.add(modeField);

        panel.add(new JLabel("Amount Paid:"));
        panel.add(amountField);

        panel.add(new JLabel("Payment Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Payment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE PAYMENT SET " +
                        "maintenance_id=?, payment_date=?, payment_mode=?, " +
                        "amount_paid=?, payment_status=? " +
                        "WHERE payment_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(maintenanceIdField.getText()));
                ps.setString(2, dateField.getText());
                ps.setString(3, modeField.getText());
                ps.setBigDecimal(4,
                        new java.math.BigDecimal(amountField.getText()));
                ps.setString(5, statusField.getText());
                ps.setInt(6, paymentId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Payment updated successfully!"
                );

                model.setRowCount(0);
                loadPayments();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // DELETE PAYMENT
    private void deletePayment() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payment first."
            );

            return;
        }

        int paymentId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this payment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql =
                        "DELETE FROM PAYMENT WHERE payment_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, paymentId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Payment deleted successfully!"
                );

                model.setRowCount(0);
                loadPayments();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // SEARCH PAYMENTS
    private void searchPayments(String keyword) {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM PAYMENT " +
                    "WHERE payment_mode LIKE ? " +
                    "OR payment_status LIKE ? " +
                    "OR maintenance_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("payment_id"),
                        rs.getInt("maintenance_id"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_mode"),
                        rs.getBigDecimal("amount_paid"),
                        rs.getString("payment_status")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        new PaymentPage();
    }
}