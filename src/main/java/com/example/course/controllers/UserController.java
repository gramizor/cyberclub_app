package com.example.course.controllers;

import com.example.course.CourseApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableAutoConfiguration
public class UserController extends CourseApplication {

    @FXML
    private VBox GamesTable;

    @FXML
    private Text amountUser;

    @FXML
    private AnchorPane balancePane;

    @FXML
    private Text balanceShow;

    @FXML
    private Text error;

    @FXML
    private ComboBox<?> gameList;

    @FXML
    private AnchorPane gamesPane;

    @FXML
    private Button goToGamesButton;

    @FXML
    private VBox historyTable;

    @FXML
    private Button logOutButton;

    @FXML
    private Button openGameButton;

    @FXML
    private Button viewBalanceButton;

    @FXML
    void goToGames(ActionEvent event) {
        gamesPane.setVisible(true);
        balancePane.setVisible(false);
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/auth.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        error.getScene().getWindow().hide();
    }

    @FXML
    void openGame(ActionEvent event) {

    }

    @FXML
    void viewBalance(ActionEvent event) {
        gamesPane.setVisible(false);
        balancePane.setVisible(true);
    }
}
