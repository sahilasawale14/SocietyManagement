package admin;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public StaffPage() {

        setTitle("Staff");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Staff");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Staff");
        addButton.addActionListener(e -> openAddStaffForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editStaff());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteStaff());

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
                e -> searchStaff(searchField.getText())
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
                "Staff ID",
                "Name",
                "Role",
                "Join Date",
                "Salary",
                "Phone"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadStaff();

        setVisible(true);
    }

    private void loadStaff() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM STAFF";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("staff_id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getDate("join_date"),
                        rs.getDouble("salary"),
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

    private void openAddStaffForm() {

        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField joinDateField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField phoneField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        panel.add(new JLabel("Join Date (YYYY-MM-DD):"));
        panel.add(joinDateField);

        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Staff",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO STAFF " +
                        "(name, role, join_date, salary, phone) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nameField.getText());
                ps.setString(2, roleField.getText());
                ps.setString(3, joinDateField.getText());
                ps.setDouble(4, Double.parseDouble(salaryField.getText()));
                ps.setString(5, phoneField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Staff added successfully!"
                );

                model.setRowCount(0);
                loadStaff();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void editStaff() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a staff member first."
            );

            return;
        }

        int staffId = (int) model.getValueAt(row, 0);

        JTextField nameField =
                new JTextField(model.getValueAt(row, 1).toString());

        JTextField roleField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField joinDateField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField salaryField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField phoneField =
                new JTextField(model.getValueAt(row, 5).toString());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        panel.add(new JLabel("Join Date (YYYY-MM-DD):"));
        panel.add(joinDateField);

        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Staff",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE STAFF SET " +
                        "name=?, role=?, join_date=?, salary=?, phone=? " +
                        "WHERE staff_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nameField.getText());
                ps.setString(2, roleField.getText());
                ps.setString(3, joinDateField.getText());
                ps.setDouble(4, Double.parseDouble(salaryField.getText()));
                ps.setString(5, phoneField.getText());
                ps.setInt(6, staffId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Staff updated successfully!"
                );

                model.setRowCount(0);
                loadStaff();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void deleteStaff() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a staff member first."
            );

            return;
        }

        int staffId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this staff member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql =
                        "DELETE FROM STAFF WHERE staff_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, staffId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Staff deleted successfully!"
                );

                model.setRowCount(0);
                loadStaff();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void searchStaff(String keyword) {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM STAFF " +
                    "WHERE name LIKE ? " +
                    "OR role LIKE ? " +
                    "OR phone LIKE ? " +
                    "OR staff_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("staff_id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getDate("join_date"),
                        rs.getDouble("salary"),
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
        new StaffPage();
    }
}