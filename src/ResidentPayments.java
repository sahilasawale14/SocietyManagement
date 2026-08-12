import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResidentPayments extends JFrame {

    JTable table;
    DefaultTableModel model;
    int residentId;

    public ResidentPayments(int residentId) {

        this.residentId = residentId;

        setTitle("My Payments");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(
                new String[]{
                        "Payment ID",
                        "Date",
                        "Payment Mode",
                        "Amount",
                        "Status"
                }, 0
        );

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadPayments();

        setVisible(true);
    }

    private void loadPayments() {

        model.setRowCount(0);

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT payment_id, payment_date, payment_mode, " +
                    "amount_paid, payment_status " +
                    "FROM payment WHERE resident_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, residentId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("payment_id"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_mode"),
                        rs.getBigDecimal("amount_paid"),
                        rs.getString("payment_status")
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