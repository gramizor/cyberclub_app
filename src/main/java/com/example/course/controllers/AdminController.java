package com.example.course.controllers;

import com.example.course.CourseApplication;
import com.example.course.entities.Computer;
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
    private Text balanceUpdateADmin;

    @FXML
    private ComboBox<Integer> brokenComputer;

    @FXML
    private Text error;

    @FXML
    private Button editBalanceButton;

    @FXML
    private AnchorPane editBalancePane;

    @FXML
    private AnchorPane editBalancePane1;

    @FXML
    private Button goToReservation;

    @FXML
    private VBox historyAmount;

    @FXML
    private Button logOutButton;

    @FXML
    private ComboBox<Integer> numberComputerList;

    @FXML
    private Button reservationButton;

    @FXML
    private Text reservationInfo;

    @FXML
    private VBox reservationTables;

    @FXML
    private ComboBox<String> statusCheck;

    @FXML
    private Button statusComputerButton;

    @FXML
    private AnchorPane statusPane;

    @FXML
    private VBox statusTable;

    @FXML
    private ComboBox<?> timeReservationList;

    @FXML
    private AnchorPane reservationPane;

    @FXML
    private ComboBox<String> usernameList;

    @FXML
    private ComboBox<String> usernameList2;

    @FXML
    private Text adminBrokenText;

    @FXML
    private Text adminFixedText;

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
                balanceUpdateADmin.setText("Баланс пополнен на " + amountText);
            }
        }else {balanceUpdateADmin.setText("Выберите пользователя");}
    }

    @FXML
    void amountInputAction(ActionEvent event) {

    }

    @FXML
    void editBalance(ActionEvent event) {
        statusPane.setVisible(false);
        editBalancePane.setVisible(true);
        reservationPane.setVisible(false);
        List<String> userNameLister = userRepo.getAllUsername();
        usernameList.setItems(FXCollections.observableArrayList(userNameLister));
    }

    @FXML
    void goToReservation(ActionEvent event) {
        statusPane.setVisible(false);
        editBalancePane.setVisible(false);
        reservationPane.setVisible(true);
        List<Integer> compNumber = computerRepo.getAllComputerNumbers();
        numberComputerList.setItems(FXCollections.observableArrayList(compNumber));
        List<String> userNameLister = userRepo.getAllUsername();
        usernameList2.setItems(FXCollections.observableArrayList(userNameLister));

        timeReservationList.setItems(FXCollections.observableArrayList());
    }

    @FXML
    void reservation(ActionEvent event) {

    }

    @FXML
    void statusComputer(ActionEvent event) {
        statusPane.setVisible(true);
        editBalancePane.setVisible(false);
        reservationPane.setVisible(false);
        List<String> computerStatusList = computerRepo.getAllStatus();
        statusCheck.setItems(FXCollections.observableArrayList(computerStatusList));
        List<Integer> computerNumbers = computerRepo.getAllComputerNumbers();
        brokenComputer.setItems(FXCollections.observableArrayList(computerNumbers));
    }

    @FXML
    void statusSeclected(ActionEvent event) {

    }

    @FXML
    void usernameListChoose(ActionEvent event) {

    }


    @FXML
    void usernameListChoose2(ActionEvent event) {

    }

    @FXML
    void brokenChoose(ActionEvent event) {
        adminFixedText.setText("");
        adminBrokenText.setText("");
    }
    @FXML
    void addBroken(ActionEvent event) {
        int compNumber = 0;
        try {
            compNumber = brokenComputer.getValue();
            Computer computer = computerRepo.findByNumber(compNumber);
            if (!computer.getStatus().equals("сломан")) {
            computer.setStatus("сломан");
            adminBrokenText.setText("Статус задан");
            computerRepo.save(computer);
            List<String> computerStatusList = computerRepo.getAllStatus();
            statusCheck.setItems(FXCollections.observableArrayList(computerStatusList));
        } else {
            adminFixedText.setText("Уже сломан");
        }
        } catch (Exception e) {
            adminBrokenText.setText("Выберите компьютер");
        }
    }

    @FXML
    void addFixed(ActionEvent event) {
        int compNumber = 0;
        try {
            compNumber = brokenComputer.getValue();
            Computer computer = computerRepo.findByNumber(compNumber);
            if (!computer.getStatus().equals("свободен")) {
                computer.setStatus("свободен");
                adminFixedText.setText("Статус задан");
                computerRepo.save(computer);
                List<String> computerStatusList = computerRepo.getAllStatus();
                statusCheck.setItems(FXCollections.observableArrayList(computerStatusList));
            } else {
                adminFixedText.setText("Уже свободен");
            }
        } catch (Exception e) {
            adminFixedText.setText("Выберите компьютер");
        }
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
