
import com.mysql.jdbc.Driver;

import java.sql.*;

/**
 * Connect to Database
 * @author hany.said
 */
public class ConnectionFactory {
    public static final String URL = "jdbc:mysql://localhost:3306/VMS";
    public static final String USER = "root";
    public static final String PASS = "1234";
    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
    /**
     * Test Connection
     */
    public static void main(String[] args) throws SQLException {
//        Connection connection = ConnectionFactory.getConnection();
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");

        PreparedStatement  preparedStatement=con.prepareStatement("SELECT * FROM VMS.staff WHERE username=? AND password=?");
        preparedStatement.setString(1,"ali");
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println(resultSet.getString("staff_name"));
        }


    }
}
