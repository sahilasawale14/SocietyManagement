import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ComplaintPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ComplaintPage() {

        setTitle("Complaints");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Complaints");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Complaint");
        addButton.addActionListener(e -> openAddComplaintForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editComplaint());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteComplaint());

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
                e -> searchComplaints(searchField.getText())
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
                "Resident ID",
                "Staff ID",
                "Date",
                "Complaint Type",
                "Description",
                "Status"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadComplaints();

        setVisible(true);
    }

    private void loadComplaints() {

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM COMPLAINTS";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("complaint_id"),
                        rs.getInt("resident_id"),
                        rs.getInt("staff_id"),
                        rs.getDate("complaint_date"),
                        rs.getString("complaint_type"),
                        rs.getString("description"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    private void openAddComplaintForm() {

        JTextField residentIdField = new JTextField();
        JTextField staffIdField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField statusField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Resident ID:"));
        panel.add(residentIdField);

        panel.add(new JLabel("Staff ID:"));
        panel.add(staffIdField);

        panel.add(new JLabel("Complaint Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Complaint Type:"));
        panel.add(typeField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Complaint",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO COMPLAINTS " +
                        "(resident_id, staff_id, complaint_date, " +
                        "complaint_type, description, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(residentIdField.getText()));
                ps.setInt(2, Integer.parseInt(staffIdField.getText()));
                ps.setString(3, dateField.getText());
                ps.setString(4, typeField.getText());
                ps.setString(5, descriptionField.getText());
                ps.setString(6, statusField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint added successfully!"
                );

                model.setRowCount(0);
                loadComplaints();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void editComplaint() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a complaint first."
            );

            return;
        }

        int complaintId = (int) model.getValueAt(row, 0);

        JTextField residentIdField =
                new JTextField(model.getValueAt(row, 1).toString());

        JTextField staffIdField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField dateField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField typeField =
                new JTextField(model.getValueAt(row, 4).toString());

        JTextField descriptionField =
                new JTextField(model.getValueAt(row, 5).toString());

        JTextField statusField =
                new JTextField(model.getValueAt(row, 6).toString());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Resident ID:"));
        panel.add(residentIdField);

        panel.add(new JLabel("Staff ID:"));
        panel.add(staffIdField);

        panel.add(new JLabel("Complaint Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Complaint Type:"));
        panel.add(typeField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Complaint",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE COMPLAINTS SET " +
                        "resident_id=?, staff_id=?, complaint_date=?, " +
                        "complaint_type=?, description=?, status=? " +
                        "WHERE complaint_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(residentIdField.getText()));
                ps.setInt(2, Integer.parseInt(staffIdField.getText()));
                ps.setString(3, dateField.getText());
                ps.setString(4, typeField.getText());
                ps.setString(5, descriptionField.getText());
                ps.setString(6, statusField.getText());
                ps.setInt(7, complaintId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint updated successfully!"
                );

                model.setRowCount(0);
                loadComplaints();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void deleteComplaint() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a complaint first."
            );

            return;
        }

        int complaintId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this complaint?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql =
                        "DELETE FROM COMPLAINTS WHERE complaint_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, complaintId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint deleted successfully!"
                );

                model.setRowCount(0);
                loadComplaints();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void searchComplaints(String keyword) {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM COMPLAINTS " +
                    "WHERE complaint_type LIKE ? " +
                    "OR description LIKE ? " +
                    "OR status LIKE ? " +
                    "OR resident_id LIKE ? " +
                    "OR staff_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);
            ps.setString(5, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("complaint_id"),
                        rs.getInt("resident_id"),
                        rs.getInt("staff_id"),
                        rs.getDate("complaint_date"),
                        rs.getString("complaint_type"),
                        rs.getString("description"),
                        rs.getString("status")
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
        new ComplaintPage();
    }
}