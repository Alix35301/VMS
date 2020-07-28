package controller;

import components.Invoice;
import components.Jobsheet;
import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

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
    public static void main(String[]args) throws SQLException, TwitterException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VMS?autoReconnect=true&useSSL=false", "root", "1234");
        String acccessToken ="1144539018970845185-QXDZQhs7zkYdhfDK3aKYKhbU9EwKo7";
        String scret ="WqPsOC8iEFuARHa9GKS6PiGMmEkyH1JxNYf7psDjQMhgH";

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey("rqdTMaC3TmrlefJTRvyM0qNvz")
            .setOAuthConsumerSecret("UGCwFQT3F6TsaZChLp7WUvBLzrGObzx1IPntgvq4PO7syIU6TN")
            .setOAuthAccessToken("1144539018970845185-QXDZQhs7zkYdhfDK3aKYKhbU9EwKo7")
            .setOAuthAccessTokenSecret("WqPsOC8iEFuARHa9GKS6PiGMmEkyH1JxNYf7psDjQMhgH");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query("Auto parts");
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            status.getFavoriteCount();

        }

        /*
        * API key:
rqdTMaC3TmrlefJTRvyM0qNvz

API secret key:
UGCwFQT3F6TsaZChLp7WUvBLzrGObzx1IPntgvq4PO7syIU6TN
* */

    }


}
