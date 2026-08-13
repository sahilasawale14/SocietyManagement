package resident;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class ResidentsPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ResidentsPage() {

        setTitle("Residents");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Residents");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Resident");
        addButton.addActionListener(e -> openAddResidentForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editResident());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteResident());

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(buttons, BorderLayout.EAST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchResidents(searchField.getText()));

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
                "ID", "Flat ID", "Name", "Phone",
                "Relation", "Gender", "Age"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadResidents();

        setVisible(true);
    }

    private void loadResidents() {

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM RESIDENT";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("resident_id"),
                        rs.getInt("flat_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relation_with_owner"),
                        rs.getString("gender"),
                        rs.getInt("age")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAddResidentForm() {

        JTextField flatIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField relationField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField ageField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Flat ID:"));
        panel.add(flatIdField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Relation:"));
        panel.add(relationField);

        panel.add(new JLabel("Gender:"));
        panel.add(genderField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Resident",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO RESIDENT " +
                        "(flat_id, name, phone, relation_with_owner, gender, age) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(flatIdField.getText()));
                ps.setString(2, nameField.getText());
                ps.setString(3, phoneField.getText());
                ps.setString(4, relationField.getText());
                ps.setString(5, genderField.getText());
                ps.setInt(6, Integer.parseInt(ageField.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Resident added successfully!"
                );

                model.setRowCount(0);
                loadResidents();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void editResident() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a resident first.");
            return;
        }

        int residentId = (int) model.getValueAt(row, 0);

        JTextField nameField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField phoneField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField relationField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField genderField =
                new JTextField(model.getValueAt(row, 5).toString());

        JTextField ageField =
                new JTextField(model.getValueAt(row, 6).toString());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Relation:"));
        panel.add(relationField);

        panel.add(new JLabel("Gender:"));
        panel.add(genderField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Resident",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE RESIDENT SET " +
                        "name=?, phone=?, relation_with_owner=?, gender=?, age=? " +
                        "WHERE resident_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nameField.getText());
                ps.setString(2, phoneField.getText());
                ps.setString(3, relationField.getText());
                ps.setString(4, genderField.getText());
                ps.setInt(5, Integer.parseInt(ageField.getText()));
                ps.setInt(6, residentId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Resident updated successfully!"
                );

                model.setRowCount(0);
                loadResidents();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void deleteResident() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a resident first.");
            return;
        }

        int residentId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this resident?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "DELETE FROM RESIDENT WHERE resident_id=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, residentId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Resident deleted successfully!"
                );

                model.setRowCount(0);
                loadResidents();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void searchResidents(String keyword) {

        model.setRowCount(0);

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM RESIDENT " +
                    "WHERE name LIKE ? OR phone LIKE ? OR flat_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("resident_id"),
                        rs.getInt("flat_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relation_with_owner"),
                        rs.getString("gender"),
                        rs.getInt("age")
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
        new ResidentsPage();
    }
}