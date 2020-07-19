package controller;

import components.Jobsheet;

import javax.print.attribute.standard.JobSheets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author ali
 * @created_on 7/16/20
 */
public class test {
    public static void main(String[]args) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
//        Jobsheet.toPDF();

        System.out.println(JobsheetController.getMechanicID("Abdulla"));
    }
}
