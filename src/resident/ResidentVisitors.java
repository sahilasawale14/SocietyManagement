package resident;
import util.DatabaseConnection;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResidentVisitors extends JFrame {

    JTable table;
    DefaultTableModel model;
    int residentId;

    public ResidentVisitors(int residentId) {

        this.residentId = residentId;

        setTitle("My Visitors");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(
                new String[]{
                        "Visitor ID",
                        "Visitor Name",
                        "Entry Time",
                        "Exit Time",
                        "Purpose",
                        "Phone"
                }, 0
        );

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadVisitors();

        setVisible(true);
    }

    private void loadVisitors() {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT v.visitor_id, v.visitor_name, " +
                    "v.entry_time, v.exit_time, v.purpose, v.phone " +
                    "FROM visitor v " +
                    "JOIN resident r ON v.flat_id = r.flat_id " +
                    "WHERE r.resident_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, residentId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("visitor_id"),
                        rs.getString("visitor_name"),
                        rs.getTimestamp("entry_time"),
                        rs.getTimestamp("exit_time"),
                        rs.getString("purpose"),
                        rs.getString("phone")
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
