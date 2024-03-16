package classes;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataBase_Connect {
    private static Connection conn;
    
    public static Connection getJDbConnection(){
        
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "Cirioto@#2023";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase_Connect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase_Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
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
