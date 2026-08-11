import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    Color sidebarColor = new Color(30, 41, 59);
    Color backgroundColor = new Color(241, 245, 249);
    Color cardColor = Color.WHITE;

    public Dashboard() {

        setTitle("Society Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN FRAME
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 650));
        sidebar.setBackground(sidebarColor);
        sidebar.setLayout(new BorderLayout());

        JLabel logo = new JLabel("  🏢 Society Manager");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        sidebar.add(logo, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setBackground(sidebarColor);
        menu.setLayout(new GridLayout(7, 1, 0, 8));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));

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

            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(sidebarColor);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setHorizontalAlignment(SwingConstants.LEFT);

            menu.add(button);
        }

        sidebar.add(menu, BorderLayout.CENTER);

        // ================= CONTENT =================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(backgroundColor);
        content.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel welcome = new JLabel("Welcome, Admin 👋");
        welcome.setFont(new Font("Arial", Font.PLAIN, 15));

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);

        // ================= CARDS =================
        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 20));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        cards.add(createCard("32", "Total Flats"));
        cards.add(createCard("18", "Residents"));
        cards.add(createCard("5", "Visitors Today"));
        cards.add(createCard("3", "Pending Complaints"));

        content.add(cards, BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(cardColor);
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel recent = new JLabel("Recent Activity");
        recent.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel activity = new JLabel(
                "<html>• New resident added<br><br>" +
                        "• Maintenance payment received<br><br>" +
                        "• Complaint registered</html>"
        );

        bottom.add(recent, BorderLayout.NORTH);
        bottom.add(activity, BorderLayout.CENTER);

        content.add(bottom, BorderLayout.SOUTH);

        // ADD TO FRAME
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    // CARD CREATOR
    private JPanel createCard(String number, String text) {

        JPanel card = new JPanel();
        card.setBackground(cardColor);
        card.setLayout(new BorderLayout());

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        card.add(numberLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);

        return card;
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}