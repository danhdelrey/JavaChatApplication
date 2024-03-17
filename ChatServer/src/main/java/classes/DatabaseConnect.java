package classes;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {

    //private static Connection conn;
    public static Connection getJDbConnection() {

        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "123456789";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    //ví dụ
    public static boolean verifyLogin(String userName, String psw) {
        return true;
    }

//    public static void main(String[] args){
//        conn = getJDbConnection();
//
//        if(conn != null){
//            System.out.println("Thanh cong");
//        }else{
//            System.out.println("That bai");
//        }
//    }
}
