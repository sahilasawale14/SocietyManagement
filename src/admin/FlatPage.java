package admin;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FlatPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public FlatPage() {

        setTitle("Flats");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Flats");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Flat");
        addButton.addActionListener(e -> openAddFlatForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editFlat());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteFlat());

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
                e -> searchFlats(searchField.getText())
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
                "ID", "Flat No", "Block",
                "Floor", "Owner ID", "Occupancy"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadFlats();

        setVisible(true);
    }

    private void loadFlats() {

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM FLAT";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("flat_id"),
                        rs.getString("flat_no"),
                        rs.getString("block"),
                        rs.getInt("floor"),
                        rs.getInt("owner_id"),
                        rs.getString("occupancy_status")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    private void openAddFlatForm() {

        JTextField flatNoField = new JTextField();
        JTextField blockField = new JTextField();
        JTextField floorField = new JTextField();
        JTextField ownerIdField = new JTextField();
        JTextField occupancyField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Flat No:"));
        panel.add(flatNoField);

        panel.add(new JLabel("Block:"));
        panel.add(blockField);

        panel.add(new JLabel("Floor:"));
        panel.add(floorField);

        panel.add(new JLabel("Owner ID:"));
        panel.add(ownerIdField);

        panel.add(new JLabel("Occupancy Status:"));
        panel.add(occupancyField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Flat",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO FLAT " +
                        "(flat_no, block, floor, owner_id, occupancy_status) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, flatNoField.getText());
                ps.setString(2, blockField.getText());
                ps.setInt(3, Integer.parseInt(floorField.getText()));
                ps.setInt(4, Integer.parseInt(ownerIdField.getText()));
                ps.setString(5, occupancyField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Flat added successfully!"
                );

                model.setRowCount(0);
                loadFlats();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void editFlat() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a flat first."
            );
            return;
        }

        int flatId = (int) model.getValueAt(row, 0);

        JTextField flatNoField =
                new JTextField(model.getValueAt(row, 1).toString());

        JTextField blockField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField floorField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField ownerIdField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField occupancyField =
                new JTextField(model.getValueAt(row, 5).toString());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Flat No:"));
        panel.add(flatNoField);

        panel.add(new JLabel("Block:"));
        panel.add(blockField);

        panel.add(new JLabel("Floor:"));
        panel.add(floorField);

        panel.add(new JLabel("Owner ID:"));
        panel.add(ownerIdField);

        panel.add(new JLabel("Occupancy Status:"));
        panel.add(occupancyField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Flat",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE FLAT SET " +
                        "flat_no=?, block=?, floor=?, owner_id=?, " +
                        "occupancy_status=? WHERE flat_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, flatNoField.getText());
                ps.setString(2, blockField.getText());
                ps.setInt(3, Integer.parseInt(floorField.getText()));
                ps.setInt(4, Integer.parseInt(ownerIdField.getText()));
                ps.setString(5, occupancyField.getText());
                ps.setInt(6, flatId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Flat updated successfully!"
                );

                model.setRowCount(0);
                loadFlats();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void deleteFlat() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a flat first."
            );
            return;
        }

        int flatId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this flat?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "DELETE FROM FLAT WHERE flat_id=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, flatId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Flat deleted successfully!"
                );

                model.setRowCount(0);
                loadFlats();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void searchFlats(String keyword) {

        model.setRowCount(0);

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM FLAT " +
                    "WHERE flat_no LIKE ? " +
                    "OR block LIKE ? " +
                    "OR occupancy_status LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("flat_id"),
                        rs.getString("flat_no"),
                        rs.getString("block"),
                        rs.getInt("floor"),
                        rs.getInt("owner_id"),
                        rs.getString("occupancy_status")
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
        new FlatPage();
    }
}