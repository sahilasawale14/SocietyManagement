import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class AddResident {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Flat ID: ");
        int flatId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Relation: ");
        String relation = sc.nextLine();

        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "INSERT INTO RESIDENT " +
                    "(flat_id, name, phone, relation_with_owner, gender, age) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, flatId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, relation);
            ps.setString(5, gender);
            ps.setInt(6, age);

            ps.executeUpdate();

            System.out.println("Resident added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


