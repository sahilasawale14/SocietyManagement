
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ResidentLogin extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public ResidentLogin() {

        setTitle("Resident Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("Resident Login");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(130, 30, 220, 40);
        panel.add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(60, 100, 100, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 100, 200, 30);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(60, 150, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 200, 30);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(160, 210, 100, 35);
        panel.add(loginButton);

        loginButton.addActionListener(e -> loginResident());

        add(panel);
        setVisible(true);
    }

    private void loginResident() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password.");
            return;
        }

        try {

            Connection con = DriverManager.getConnection(
                    "jdbc" +
                            ":mysql://localhost:3306/society_management",
                    "root",
                    "Sahil" +
                            "@135678"
            );

            String sql = "SELECT * FROM RESIDENT WHERE username = ? AND password = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(this,
                        "Login successful!");

                new ResidentDashboard();

                dispose();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.");

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

    public static void main(String[] args) {
        new ResidentLogin();
    }
}

