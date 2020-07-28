package components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ali
 * @created_on 4/12/20
 */
public class Customer {
    String id;
    String type;
    String name;
    String email;
    String password;
    String phone;
    String address;
    public Customer(){

    }


    public Customer(String email, String password){
        this.email=email;
        this.password=password;
    }

    public Customer(String id, String type, String name, String email, String password, String phone, String address) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void addCustomerToDb(Customer customer, Connection connection) {
        String sql = "INSERT INTO VMS.CUSTOMERS (`customer_type`, `cust_name`, `email`, `password`, `phone`, `address`, `customer_id`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement p1 = connection.prepareStatement(sql);
            p1.setString(1, customer.getType());
            p1.setString(2, customer.getName());
            p1.setString(3, customer.getEmail());
            p1.setString(6, customer.getAddress());
            p1.setString(4, customer.getPassword());
            p1.setString(5, customer.getPhone());
            p1.setString(7, customer.getId());
            p1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isRegistered(String customer_id) {
        boolean found = false;
        try {
            String sql = "SELECT * FROM VMS.CUSTOMERS WHERE customer_id = ?";
            PreparedStatement preparedStatement = Helpers.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                found = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return found;
    }

    public String getType(int i){
        return (i==2 ? "Commercial":"Private");
    }
}
