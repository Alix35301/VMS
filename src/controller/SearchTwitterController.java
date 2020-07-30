package controller;

import components.AlertDiaglog;
import components.Jobsheet;
import components.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * @author ali
 * @created_on 7/28/20
 */
public class SearchTwitterController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField searchTxtF;
    @FXML
    private TableView resultsTable;
    @FXML
    private ChoiceBox<String>viewCountCb;
    ObservableList<String> viewCount = FXCollections.observableArrayList("5", "10","15");



    @FXML
    public void initialize() {

        TableColumn<Tweet, String> column3 = new TableColumn<>("User");
        column3.setCellValueFactory(new PropertyValueFactory<>("user"));

        TableColumn<Tweet, String> column2 = new TableColumn<>("Tweet");
        column2.setCellValueFactory(new PropertyValueFactory<>("tweet"));

        TableColumn<Tweet, String> column1 = new TableColumn<>("Date");
        column1.setCellValueFactory(new PropertyValueFactory<>("date"));

        viewCountCb.setItems(viewCount);
        viewCountCb.getSelectionModel().select("5");



        column2.setCellFactory(col -> {
            return new TableCell<Tweet, String>() {
                @Override
                public void updateItem(String tweet, boolean empty) {
                    super.updateItem(tweet, empty);
                    if (tweet != null) {
                        Text text = new Text(tweet);
                        text.setStyle("-fx-text-wrap: true;" +
                            " -fx-padding: 5px 30px 5px 5px;" +
                            " -fx-text-alignment:left;");
                        text.setWrappingWidth(col.getWidth() + 5);
                        this.setPrefHeight(text.getLayoutBounds().getHeight());
                        this.setGraphic(text);
                    }
                }
            };
        });

        resultsTable.getColumns().addAll(column1, column2, column3);


    }

    public void search(ActionEvent event) throws TwitterException {

        resultsTable.getItems().clear();
        Twitter twitter = setupTwitter();
        if (searchTxtF.getText().trim().isEmpty()) {
            AlertDiaglog.infoBox("Please enter something to search", "Empty search", Alert.AlertType.ERROR);
        } else {
            Query query = new Query(searchTxtF.getText());

            QueryResult result = twitter.search(query);
            int limit = Integer.parseInt(viewCountCb.getValue());

            for (int i = 0; i < limit; i++) {
                Status status = result.getTweets().get(i);
                Tweet tweet = new Tweet();
                    tweet.setUser("@" + status.getUser().getScreenName());
                    tweet.setTweet(status.getText());
                    tweet.setDate(status.getCreatedAt().toString());
                    resultsTable.getItems().add(tweet);



            }


        }
    }


    public Twitter setupTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey("rqdTMaC3TmrlefJTRvyM0qNvz")
            .setOAuthConsumerSecret("UGCwFQT3F6TsaZChLp7WUvBLzrGObzx1IPntgvq4PO7syIU6TN")
            .setOAuthAccessToken("1144539018970845185-QXDZQhs7zkYdhfDK3aKYKhbU9EwKo7")
            .setOAuthAccessTokenSecret("WqPsOC8iEFuARHa9GKS6PiGMmEkyH1JxNYf7psDjQMhgH");
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();

    }

    public void logout(ActionEvent event) throws IOException {

        AlertDiaglog.infoBox("You have successfully logged out.","Loggout", Alert.AlertType.INFORMATION);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Login.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);

    }

    public void back(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/analyst.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);


    }

}
