package yazlab1proje1;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DbHelper {
    static String username ="root";
    static String password="1234";
    static String dbUrl="jdbc:mysql://130.211.228.7:3306/transport";
    
    public static Connection getConnection() throws SQLException{
        return(Connection) DriverManager.getConnection(dbUrl, username,password);
    }
    public void ShowError(SQLException exception){
       System.out.println("Error: "+exception.getMessage());
       System.out.println("Error code: "+exception.getErrorCode());
    }
}
