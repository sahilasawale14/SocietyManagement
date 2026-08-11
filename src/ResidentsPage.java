import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class ResidentsPage extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ResidentsPage() {

        setTitle("Residents");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Residents");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Resident");
        addButton.addActionListener(e -> openAddResidentForm());

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(addButton, BorderLayout.EAST);

        mainPanel.add(top, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "ID", "Flat ID", "Name", "Phone",
                "Relation", "Gender", "Age"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(mainPanel);

        loadResidents();

        setVisible(true);
    }

    private void loadResidents() {

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM RESIDENT";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("resident_id"),
                        rs.getInt("flat_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relation_with_owner"),
                        rs.getString("gender"),
                        rs.getInt("age")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAddResidentForm() {

        JTextField flatIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField relationField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField ageField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Flat ID:"));
        panel.add(flatIdField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Relation:"));
        panel.add(relationField);

        panel.add(new JLabel("Gender:"));
        panel.add(genderField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Resident",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {
                Connection con = DatabaseConnection.getConnection();

                String sql = "INSERT INTO RESIDENT " +
                        "(flat_id, name, phone, relation_with_owner, gender, age) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, Integer.parseInt(flatIdField.getText()));
                ps.setString(2, nameField.getText());
                ps.setString(3, phoneField.getText());
                ps.setString(4, relationField.getText());
                ps.setString(5, genderField.getText());
                ps.setInt(6, Integer.parseInt(ageField.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Resident added successfully!"
                );

                model.setRowCount(0);
                loadResidents();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    public static void main(String[] args) {
        new ResidentsPage();
    }
}