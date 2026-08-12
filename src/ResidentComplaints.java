import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResidentComplaints extends JFrame {

    JTable table;
    DefaultTableModel model;
    int residentId;

    public ResidentComplaints(int residentId) {

        this.residentId = residentId;

        setTitle("My Complaints");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(
                new String[]{"Complaint ID", "Date", "Type", "Description", "Status"}, 0
        );

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Complaint");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addComplaint());
        editButton.addActionListener(e -> editComplaint());
        deleteButton.addActionListener(e -> deleteComplaint());
        refreshButton.addActionListener(e -> loadComplaints());

        loadComplaints();

        setVisible(true);
    }


    private void loadComplaints() {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT complaint_id, complaint_date, complaint_type, " +
                    "description, status FROM complaints WHERE resident_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, residentId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("complaint_id"),
                        rs.getDate("complaint_date"),
                        rs.getString("complaint_type"),
                        rs.getString("description"),
                        rs.getString("status")
                });
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());
        }
    }

    private void addComplaint() {

        JTextField typeField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 20);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.add(new JLabel("Complaint Type:"), BorderLayout.NORTH);
        panel.add(typeField, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Description:"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JPanel main = new JPanel(new GridLayout(2, 1, 10, 10));
        main.add(panel);
        main.add(bottom);

        int result = JOptionPane.showConfirmDialog(
                this,
                main,
                "Add Complaint",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION)
            return;

        String type = typeField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (type.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
            return;
        }

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "INSERT INTO complaints " +
                    "(resident_id, complaint_date, complaint_type, description, status) " +
                    "VALUES (?, CURDATE(), ?, ?, 'Pending')";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, residentId);
            pst.setString(2, type);
            pst.setString(3, description);

            pst.executeUpdate();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(this,
                    "Complaint added successfully!");

            loadComplaints();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());
        }
    }

    private void editComplaint() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a complaint first.");
            return;
        }

        String status = model.getValueAt(row, 4).toString();

        if (!status.equalsIgnoreCase("Pending")) {
            JOptionPane.showMessageDialog(this,
                    "Only pending complaints can be edited.");
            return;
        }

        int complaintId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        JTextField typeField = new JTextField(
                model.getValueAt(row, 2).toString()
        );

        JTextArea descriptionArea = new JTextArea(
                model.getValueAt(row, 3).toString(), 5, 20
        );

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(new JLabel("Complaint Type:"));
        panel.add(typeField);

        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Complaint",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION)
            return;

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "UPDATE complaints SET complaint_type = ?, " +
                    "description = ? WHERE complaint_id = ? " +
                    "AND resident_id = ? AND status = 'Pending'";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, typeField.getText());
            pst.setString(2, descriptionArea.getText());
            pst.setInt(3, complaintId);
            pst.setInt(4, residentId);

            pst.executeUpdate();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(this,
                    "Complaint updated!");

            loadComplaints();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());
        }
    }

    private void deleteComplaint() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a complaint first.");
            return;
        }

        String status = model.getValueAt(row, 4).toString();

        if (!status.equalsIgnoreCase("Pending")) {
            JOptionPane.showMessageDialog(this,
                    "Only pending complaints can be deleted.");
            return;
        }

        int complaintId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this complaint?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION)
            return;

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "DELETE FROM complaints " +
                    "WHERE complaint_id = ? AND resident_id = ? " +
                    "AND status = 'Pending'";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, complaintId);
            pst.setInt(2, residentId);

            pst.executeUpdate();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(this,
                    "Complaint deleted!");

            loadComplaints();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());
        }
    }
}