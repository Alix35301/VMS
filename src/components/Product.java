package components;

/**
 * @author ali
 * @created_on 7/21/20
 */
public class Product {
    String id, productCode, price;

    public Product(){};

    public Product(String id, String productCode, String price){
        this.id = id;
        this.productCode =productCode;
        this.price =price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
