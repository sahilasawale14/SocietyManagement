import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResidentNotices extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ResidentNotices() {

        setTitle("Society Notices");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(
                new String[]{
                        "Notice ID",
                        "Title",
                        "Description",
                        "Publish Date"
                }, 0
        );

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadNotices();

        setVisible(true);
    }

    private void loadNotices() {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT notice_id, title, description, publish_date " +
                    "FROM notice ORDER BY publish_date DESC";

            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("notice_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("publish_date")
                });
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());

            ex.printStackTrace();
        }
    }
}