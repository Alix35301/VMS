package test;

import components.InvoiceGenerator;
import components.Product;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/29/20
 */
public class TestInvoiceGenerator {

    Product productOne = new Product("1","Tyre","500");
    Product productTwo = new Product("2", "wash","70");

    ArrayList<Product> products = new ArrayList<>();
    public static final float DELTA = (float) 1e-15;


    @Test
    public void testCalculateTotal(){
        products.add(productOne);
        products.add(productTwo);

        for (Product product:products){
            assertEquals(550,InvoiceGenerator.calculateTotal(products),DELTA);
        }
    }
}
