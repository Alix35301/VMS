package test;
import controller.test;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author ali
 * @created_on 7/29/20
 */
public class TestJunit {
    String message =" Hello";
    controller.test test = new test(message);
    @Test
    public void testPrintMessage(){
        assertEquals(message, test.printMessage());
    }
}
