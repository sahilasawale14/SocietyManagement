import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class NoticePage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public NoticePage() {

        setTitle("Notices");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Notices");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Notice");
        addButton.addActionListener(e -> openAddNoticeForm());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editNotice());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteNotice());

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
                e -> searchNotices(searchField.getText())
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
                "Admin ID",
                "Title",
                "Description",
                "Publish Date"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadNotices();

        setVisible(true);
    }

    // ================= LOAD NOTICES =================

    private void loadNotices() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM NOTICE";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("notice_id"),
                        rs.getObject("admin_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("publish_date")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    // ================= ADD NOTICE =================

    private void openAddNoticeForm() {

        JTextField adminIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Admin ID:"));
        panel.add(adminIdField);

        panel.add(new JLabel("Title:"));
        panel.add(titleField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Publish Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Notice",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO NOTICE " +
                        "(admin_id, title, description, publish_date) " +
                        "VALUES (?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                // Admin ID can be empty because your table allows NULL
                if (adminIdField.getText().trim().isEmpty()) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, Integer.parseInt(adminIdField.getText()));
                }

                ps.setString(2, titleField.getText());
                ps.setString(3, descriptionField.getText());
                ps.setString(4, dateField.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Notice added successfully!"
                );

                model.setRowCount(0);
                loadNotices();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // ================= EDIT NOTICE =================

    private void editNotice() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a notice first."
            );

            return;
        }

        int noticeId = (int) model.getValueAt(row, 0);

        JTextField adminIdField =
                new JTextField(
                        model.getValueAt(row, 1) == null
                                ? ""
                                : model.getValueAt(row, 1).toString()
                );

        JTextField titleField =
                new JTextField(model.getValueAt(row, 2).toString());

        JTextField descriptionField =
                new JTextField(model.getValueAt(row, 3).toString());

        JTextField dateField =
                new JTextField(model.getValueAt(row, 4).toString());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Admin ID:"));
        panel.add(adminIdField);

        panel.add(new JLabel("Title:"));
        panel.add(titleField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Publish Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Notice",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql = "UPDATE NOTICE SET " +
                        "admin_id=?, title=?, description=?, publish_date=? " +
                        "WHERE notice_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                if (adminIdField.getText().trim().isEmpty()) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, Integer.parseInt(adminIdField.getText()));
                }

                ps.setString(2, titleField.getText());
                ps.setString(3, descriptionField.getText());
                ps.setString(4, dateField.getText());
                ps.setInt(5, noticeId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Notice updated successfully!"
                );

                model.setRowCount(0);
                loadNotices();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // ================= DELETE NOTICE =================

    private void deleteNotice() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a notice first."
            );

            return;
        }

        int noticeId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this notice?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {

                Connection con = DatabaseConnection.getConnection();

                String sql =
                        "DELETE FROM NOTICE WHERE notice_id=?";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, noticeId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Notice deleted successfully!"
                );

                model.setRowCount(0);
                loadNotices();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // ================= SEARCH NOTICES =================

    private void searchNotices(String keyword) {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM NOTICE " +
                    "WHERE title LIKE ? " +
                    "OR description LIKE ? " +
                    "OR admin_id LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("notice_id"),
                        rs.getObject("admin_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("publish_date")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {
        new NoticePage();
    }
}