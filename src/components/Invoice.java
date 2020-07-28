package components;

/**
 * @author ali
 * @created_on 7/21/20
 */
public class Invoice {
    String id, vehicleReg, customerId,createdUser,createdDate,status, paymentMethod, amountGiven;

    public String getAmountGiven() {
        return amountGiven;
    }

    public void setAmountGiven(String amountGiven) {
        this.amountGiven = amountGiven;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleReg() {
        return vehicleReg;
    }

    public void setVehicleReg(String vehicleReg) {
        this.vehicleReg = vehicleReg;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
