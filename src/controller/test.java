package controller;

import components.Invoice;
import components.Jobsheet;
import javafx.application.Application;
import javafx.application.HostServices;
import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

import javax.print.attribute.standard.JobSheets;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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

    private String message;

    public test(String message) {
        this.message = message;
    }
    public String printMessage(){
        System.out.println(message);
        return message;
    }

    public static void main(String[]args) throws SQLException, TwitterException, IOException {


        /*
        * API key:
rqdTMaC3TmrlefJTRvyM0qNvz

API secret key:
UGCwFQT3F6TsaZChLp7WUvBLzrGObzx1IPntgvq4PO7syIU6TN
* */

    }


}
