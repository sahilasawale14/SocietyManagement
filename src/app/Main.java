package app;

import admin.AdminLogin;
import resident.ResidentLogin;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {

        setTitle("Society Management System");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel title = new JLabel("Society Management System");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JButton adminButton = new JButton("Admin");
        JButton residentButton = new JButton("Resident");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(adminButton, gbc);

        gbc.gridx = 1;
        panel.add(residentButton, gbc);

        add(panel);

        adminButton.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });

        residentButton.addActionListener(e -> {
            dispose();
            new ResidentLogin();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}