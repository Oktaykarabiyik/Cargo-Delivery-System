package yazlab1proje1;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DbHelper {
    static String username ="---------";
    static String password="------------";
    static String dbUrl="----------------";
    
    public static Connection getConnection() throws SQLException{
        return(Connection) DriverManager.getConnection(dbUrl, username,password);
    }
    public void ShowError(SQLException exception){
       System.out.println("Error: "+exception.getMessage());
       System.out.println("Error code: "+exception.getErrorCode());
    }
}
