package components;

import java.sql.*;

/**
 * @author ali
 * @created_on 4/18/20
 */
public class Auth {
    public static int loggedOn = 0;
    private static String email;

    public static String getEmail() {
        return email;
    }

    public static User loggedUser = new User();


    public static boolean Authenticate(User user) throws SQLException {
        boolean status = false;
        email = user.getEmail();

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        PreparedStatement preparedStatement;
        if (user.getUsertype().equals("Staff")) {
            preparedStatement = con.prepareStatement("SELECT * FROM VMS.staffs WHERE email=? AND password=?");
            getLoginStatement(preparedStatement, user);
        } else {
            preparedStatement = con.prepareStatement("SELECT * FROM VMS.customers WHERE email=? AND password=?");
            getLoginStatement(preparedStatement, user);

        }
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            loggedOn = 1;
            loggedUser.setName(resultSet.getString(3));
            status = true;
        }
        return status;
    }

    public static PreparedStatement getLoginStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getPassword());
        return preparedStatement;
    }

    public static String getUser() {
        String name = null;
        if (loggedOn == 1) {
            name = loggedUser.getName();
        }
        return name;
    }
}
