import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Society Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 5, 5));

        String[] buttons = {
                "Dashboard",
                "Residents",
                "Flats",
                "Visitors",
                "Complaints",
                "Payments",
                "Notices"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            sidebar.add(button);
        }

        // Content
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(2, 2, 20, 20));

        content.add(new JLabel("Welcome, Admin 👋"));
        content.add(new JLabel("Total Flats: 32"));
        content.add(new JLabel("Total Residents"));
        content.add(new JLabel("Pending Complaints"));

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}