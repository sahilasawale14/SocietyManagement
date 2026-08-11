
import javax.swing.*;
import java.awt.*;

public class ResidentDashboard extends JFrame {

    public ResidentDashboard() {

        setTitle("Resident Dashboard");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249));

        JLabel title = new JLabel("Resident Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 20));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        buttonPanel.setBackground(new Color(241, 245, 249));

        JButton profileButton = new JButton("My Profile");
        JButton paymentButton = new JButton("My Payments");
        JButton complaintButton = new JButton("My Complaints");
        JButton visitorButton = new JButton("My Visitors");
        JButton noticeButton = new JButton("Notices");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(profileButton);
        buttonPanel.add(paymentButton);
        buttonPanel.add(complaintButton);
        buttonPanel.add(visitorButton);
        buttonPanel.add(noticeButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> {
            dispose();
            new ResidentLogin();
        });

        add(mainPanel);
        setVisible(true);
    }
}

