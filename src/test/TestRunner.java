package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author ali
 * @created_on 7/29/20
 */
public class TestRunner {
    public static void main(String[] args){
        Result result = JUnitCore.runClasses(TestInvoiceGenerator.class);
        for (Failure failure: result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
