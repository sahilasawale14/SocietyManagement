package admin;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewResidents {
    public static void main(String[] args) {

        try {
            Connection con = DatabaseConnection.getConnection();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM RESIDENT");

            while(rs.next()) {
                System.out.println(rs.getString("name"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}