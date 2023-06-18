package com.example.course.controllers;

import com.example.course.CourseApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableAutoConfiguration
public class AdminController extends CourseApplication {

    @FXML
    private Button addBalanceButton;

    @FXML
    private TextField amountInput;

    @FXML
    private Button editBalanceButton;

    @FXML
    private AnchorPane editBalancePane;
    @FXML
    private Text error;

    @FXML
    private VBox historyAmount;

    @FXML
    private Button logOutButton;

    @FXML
    private ChoiceBox<?> statusChoose;

    @FXML
    private Button statusComputerButton;

    @FXML
    private AnchorPane statusPane;

    @FXML
    private VBox statusTable;

    @FXML
    private ComboBox<?> usernameList;

    @FXML
    void addBalance(ActionEvent event) {

    }

    @FXML
    void editBalance(ActionEvent event) {
        statusPane.setVisible(false);
        editBalancePane.setVisible(true);
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
    void statusComputer(ActionEvent event) {
        statusPane.setVisible(true);
        editBalancePane.setVisible(false);
    }

    @FXML
    void usernameListChoose(ActionEvent event) {

    }
}
