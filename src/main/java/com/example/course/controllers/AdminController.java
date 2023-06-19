package com.example.course.controllers;

import com.example.course.CourseApplication;
import com.example.course.entities.PaymentHistory;
import com.example.course.entities.User;
import com.example.course.repo.ComputerRepo;
import com.example.course.repo.PaymentRepo;
import com.example.course.repo.UserRepo;
import javafx.collections.FXCollections;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    private ComboBox<String> statusCheck;

    @FXML
    private Button statusComputerButton;

    @FXML
    private AnchorPane statusPane;

    @FXML
    private VBox statusTable;

    @FXML
    private ComboBox<String> usernameList;

    @Autowired
    private ComputerRepo computerRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PaymentRepo paymentRepo;

    @FXML
    void addBalance(ActionEvent event) {
        String selectedUsername = usernameList.getValue();
        if (selectedUsername != null) {
            User user = userRepo.findByUsername(selectedUsername);
            if (user != null) {
                String amountText = amountInput.getText();
                double amount = Double.parseDouble(amountText);
                double newBalance = user.getBalance() + amount;
                user.setBalance(newBalance);
                userRepo.save(user);
                PaymentHistory payment = new PaymentHistory();
                payment.setDate(LocalDate.now());
                payment.setTime(Time.valueOf(LocalTime.now()));
                payment.setAmount(amount);
                payment.setUser(user);
                paymentRepo.save(payment);
            }
        }
    }

    @FXML
    void amountInputAction(ActionEvent event) {

    }

    @FXML
    void editBalance(ActionEvent event) {
        statusPane.setVisible(false);
        editBalancePane.setVisible(true);
        List<String> userNameLister = userRepo.getAllUsername();
        usernameList.setItems(FXCollections.observableArrayList(userNameLister));
    }


    @FXML
    void statusComputer(ActionEvent event) {
        statusPane.setVisible(true);
        editBalancePane.setVisible(false);
        List<String> computerStatusList = computerRepo.getAllStatus();
        statusCheck.setItems(FXCollections.observableArrayList(computerStatusList));
    }

    @FXML
    void statusSeclected(ActionEvent event) {

    }

    @FXML
    void usernameListChoose(ActionEvent event) {

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
}
