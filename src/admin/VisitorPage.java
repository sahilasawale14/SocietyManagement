package admin;
import util.DatabaseConnection;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VisitorPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public VisitorPage() {

        setTitle("Visitors");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Visitors");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Visitor");
        addButton.addActionListener(e -> openAddVisitorForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editVisitor());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteVisitor());

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
                e -> searchVisitors(searchField.getText())
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
                "ID", "Flat ID", "Visitor Name",
                "Entry Time", "Exit Time", "Purpose", "Phone"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadVisitors();

        setVisible(true);
    }

    private void loadVisitors() {

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM VISITOR";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("visitor_id"),
                        rs.getInt("flat_id"),
                        rs.getString("visitor_name"),
                        rs.getTime("entry_time"),
                        rs.getTime("exit_time"),
                        rs.getString("purpose"),
                        rs.getString("phone")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    private void openAddVisitorForm() {

        JTextField flatIdField = new JTextField();
        JTextField visitorNameField = new JTextField();
        JTextField entryTimeField = new JTextField();
        JTextField exitTimeField = new JTextField();
        JTextField purposeField = new JTextField();
        JTextField phoneField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Flat ID:"));
        panel.add(flatIdField);

        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);

        panel.add(new JLabel("Entry Time (HH:MM:SS):"));
        panel.add(entryTimeField);

        panel.add(new JLabel("Exit Time (HH:MM:SS):"));
        panel.add(exitTimeField);

        panel.add(new JLabel("Purpose:"));
        panel.add(purposeField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Visitor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO VISITOR " +
                        "(flat_id, visitor_name, entry_time, exit_time, purpose, phone) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(flatIdField.getText()));
                ps.setString(2, visitorNameField.getText());
                ps.setString(3, entryTimeField.getText());
                ps.setString(4, exitTimeField.getText());
                ps.setString(5, purposeField.getText());
                ps.setString(6, phoneField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Visitor added successfully!"
                );

                model.setRowCount(0);
                loadVisitors();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void editVisitor() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a visitor first."
            );
            return;
        }

        int visitorId = (int) model.getValueAt(row, 0);

        JTextField flatIdField =
                new JTextField(model.getValueAt(row, 1).toString());

        JTextField visitorNameField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField entryTimeField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField exitTimeField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField purposeField =
                new JTextField(model.getValueAt(row, 5).toString());

        JTextField phoneField =
                new JTextField(model.getValueAt(row, 6).toString());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Flat ID:"));
        panel.add(flatIdField);

        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);

        panel.add(new JLabel("Entry Time (HH:MM:SS):"));
        panel.add(entryTimeField);

        panel.add(new JLabel("Exit Time (HH:MM:SS):"));
        panel.add(exitTimeField);

        panel.add(new JLabel("Purpose:"));
        panel.add(purposeField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Visitor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE VISITOR SET " +
                        "flat_id=?, visitor_name=?, entry_time=?, " +
                        "exit_time=?, purpose=?, phone=? " +
                        "WHERE visitor_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(flatIdField.getText()));
                ps.setString(2, visitorNameField.getText());
                ps.setString(3, entryTimeField.getText());
                ps.setString(4, exitTimeField.getText());
                ps.setString(5, purposeField.getText());
                ps.setString(6, phoneField.getText());
                ps.setInt(7, visitorId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Visitor updated successfully!"
                );

                model.setRowCount(0);
                loadVisitors();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void deleteVisitor() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a visitor first."
            );
            return;
        }

        int visitorId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this visitor?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "DELETE FROM VISITOR WHERE visitor_id=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, visitorId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Visitor deleted successfully!"
                );

                model.setRowCount(0);
                loadVisitors();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void searchVisitors(String keyword) {

        model.setRowCount(0);

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM VISITOR " +
                    "WHERE visitor_name LIKE ? " +
                    "OR purpose LIKE ? " +
                    "OR phone LIKE ? " +
                    "OR flat_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("visitor_id"),
                        rs.getInt("flat_id"),
                        rs.getString("visitor_name"),
                        rs.getTime("entry_time"),
                        rs.getTime("exit_time"),
                        rs.getString("purpose"),
                        rs.getString("phone")
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
        new VisitorPage();
    }
}