package resident;
import util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ResidentProfile extends JFrame {

    public ResidentProfile(int residentId) {

        setTitle("My Profile");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel nameLabel = new JLabel("Name:");
        JLabel flatLabel = new JLabel("Flat ID:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel relationLabel = new JLabel("Relation:");
        JLabel genderLabel = new JLabel("Gender:");
        JLabel ageLabel = new JLabel("Age:");

        JLabel nameValue = new JLabel();
        JLabel flatValue = new JLabel();
        JLabel phoneValue = new JLabel();
        JLabel relationValue = new JLabel();
        JLabel genderValue = new JLabel();
        JLabel ageValue = new JLabel();

        panel.add(nameLabel);
        panel.add(nameValue);

        panel.add(flatLabel);
        panel.add(flatValue);

        panel.add(phoneLabel);
        panel.add(phoneValue);

        panel.add(relationLabel);
        panel.add(relationValue);

        panel.add(genderLabel);
        panel.add(genderValue);

        panel.add(ageLabel);
        panel.add(ageValue);

        JButton closeButton = new JButton("Close");
        panel.add(new JLabel());
        panel.add(closeButton);

        closeButton.addActionListener(e -> dispose());

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/society_management",
                    "root",
                    "Sahil" +
                            "@135678"
            );

            String sql = "SELECT * FROM resident WHERE resident_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, residentId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameValue.setText(rs.getString("name"));
                flatValue.setText(String.valueOf(rs.getInt("flat_id")));
                phoneValue.setText(rs.getString("phone"));
                relationValue.setText(rs.getString("relation_with_owner"));
                genderValue.setText(rs.getString("gender"));
                ageValue.setText(String.valueOf(rs.getInt("age")));
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage());
        }

        add(panel);
        setVisible(true);
    }
}