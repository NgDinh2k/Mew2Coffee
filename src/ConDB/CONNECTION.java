/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConDB;


import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author My-PC
 */
public class CONNECTION {

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://MY-PC:1433;DatabaseName=QLKNL;user=sa;password=123;"
                    + "encrypt=true; trustServerCertificate=true;sslProtocol=TLSv1.2";
            Connection con = DriverManager.getConnection(URL);
            return con;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Lỗi kết nối SQL", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
