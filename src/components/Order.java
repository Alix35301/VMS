package components;

/**
 * @author ali
 * @created_on 7/21/20
 */
public class Order {
    String id, invoiceId, productId;

    public Order(Product product, Invoice invoice){
        this.invoiceId = invoice.getId();
        this.productId = product.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
