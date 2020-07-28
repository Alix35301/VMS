package components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ali
 * @created_on 7/24/20
 */
public class Vehicle {
    String id, customer_id, model, vehicle_reg, chassis_num, reg_date;
    public Vehicle(){

    }

    public Vehicle(String customer_id, String model, String vehicle_reg, String chassis_num, String reg_date) {
        this.customer_id = customer_id;
        this.model = model;
        this.vehicle_reg = vehicle_reg;
        this.chassis_num = chassis_num;
        this.reg_date = reg_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicle_reg() {
        return vehicle_reg;
    }

    public void setVehicle_reg(String vehicle_reg) {
        this.vehicle_reg = vehicle_reg;
    }

    public String getChassis_num() {
        return chassis_num;
    }

    public void setChassis_num(String chassis_num) {
        this.chassis_num = chassis_num;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }


    public static void addVehicleToDb(Vehicle vehicle, Connection connection) {
        String sql = "INSERT INTO VMS.VEHICLES (customer_id, model, vehicle_reg, chassis_num, reg_date) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement p1 = connection.prepareStatement(sql);
            p1.setString(1, vehicle.getCustomer_id());
            p1.setString(2, vehicle.getModel());
            p1.setString(3, vehicle.getVehicle_reg());
            p1.setString(4, vehicle.getChassis_num());
            p1.setString(5, vehicle.getReg_date());
            p1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean registered(String vehicle_reg){
        boolean found = false;

        String sql = "SELECT * FROM VMS.VEHICLES WHERE vehicle_reg = ?";
        try {
            PreparedStatement preparedStatement = Helpers.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, vehicle_reg);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                found = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return found;
    }
}
