package classes;

import frames.ServerFrame;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {

    private static Connection conn;

    //Kết nối tới cơ sở dữ liệu
    public static void getJDbConnection() {

        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "123456789";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);
            ServerFrame.logMessage("Connect to database successfully!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Kiểm tra các thông tin tài khoản người dùng trong Database
    public static void view_Account() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from account");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("username") + "\n" + "Pass: " + rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Kiểm tra xem username đã tồn tại chưa??
    public static boolean isUsernameExist(String username) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Account WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Nếu có kết quả trả về, tức là username đã tồn tại
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Nếu không có kết quả trả về, tức là username chưa tồn tại
        return false;
    }

    //Xác nhận thông tin đăng ký
    public static boolean Verify_dangky(String username, String password) {

        boolean status = false;

        if (isUsernameExist(username)) {
            return status;
        }

        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";  //Chèn thông tin vào bảng account

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();

            pstmt.close();

            status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    //Xác nhận thông tin đăng nhập
    public static boolean verifyLogin(String username, String password) {
        boolean status = false;

        String sql = "select * from account where username=? and password=?"; //Lấy thông tin từ bảng account

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            status = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}
