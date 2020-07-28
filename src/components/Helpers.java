package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;

/**
 * @author ali
 * @created_on 7/17/20
 */
public class Helpers {
    public static String getTime(){
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
        return current_time_str;
    }

    public static Connection getConnection(){
        Connection con =null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");

        }catch (Exception e){
            e.printStackTrace();
        }
        return con;

    }
}
