package com.example.course.controllers;

import com.example.course.CourseApplication;
import com.example.course.entities.*;
import com.example.course.repo.*;
import com.example.course.service.Storage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableAutoConfiguration
public class UserController extends CourseApplication {

    @FXML
    private VBox GamesTable;

    @FXML
    private ComboBox<Integer> amountComputerPerHourList;

    @FXML
    private Text amountItog;

    @FXML
    private Button amountItogButton;

    @FXML
    private Text amountUser;

    @FXML
    private AnchorPane balancePane;

    @FXML
    private Text balanceShow;

    @FXML
    private Text balanceUpdateText;

    @FXML
    private Button buySessionList;

    @FXML
    private VBox computerAmountTable;

    @FXML
    private Text dateEndSession;

    @FXML
    private Text error;

    @FXML
    private ComboBox<String> gameList;

    @FXML
    private AnchorPane gamesPane;

    @FXML
    private Button goToGamesButton;

    @FXML
    private TextField hoursInputButton;

    @FXML
    private VBox historyTable;

    @FXML
    private Button logOutButton;

    @FXML
    private ComboBox<Integer> numberComputerList;

    @FXML
    private ComboBox<Integer> numberComputerList2;

    @FXML
    private Button openGameButton;

    @FXML
    private AnchorPane userPane;

    @FXML
    private Text usernameShow;

    @FXML
    private Button viewBalanceButton;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ComputerRepo computerRepo;
    @Autowired
    private Storage storage;
    @Autowired
    private GameRepo gameRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private VisitRepo visitRepo;

    @FXML
    void amountComputerPerHour(ActionEvent event) {

    }

    @FXML
    void buySession(ActionEvent event) {
        int hours = Integer.parseInt(hoursInputButton.getText());
        int computerNumber = numberComputerList2.getValue();
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);
        boolean sessionExists = visitRepo.existsByUserAndComputer(user, computerRepo.findByNumber(computerNumber));
        Computer computer = computerRepo.findByNumber(computerNumber);
        if (sessionExists) {
            balanceUpdateText.setText("Сессия уже куплена, повторите позже.");
        } else {
            if (computer != null) {
                double hourlyRate = computer.getCost();
                double sessionCost = hours * hourlyRate;
                amountItog.setText(String.format("%.2f руб", sessionCost));
                double balance = user.getBalance();
                if (balance >= sessionCost) {
                    double newBalance = balance - sessionCost;
                    user.setBalance(newBalance);
                    userRepo.save(user);
                    Visit visit = new Visit();
                    visit.setDate(LocalDate.now());
                    visit.setStartTime(Time.valueOf(LocalTime.now()));
                    LocalTime endTime = LocalTime.now().plusHours(hours);
                    visit.setEndTime(Time.valueOf(endTime));
                    visit.setUser(user);
                    visit.setComputer(computerRepo.findByNumber(computerNumber));
                    visitRepo.save(visit);

                    PaymentHistory payment = new PaymentHistory();
                    payment.setDate(LocalDate.now());
                    payment.setTime(Time.valueOf(LocalTime.now()));
                    payment.setAmount(sessionCost);
                    payment.setUser(user);
                    paymentRepo.save(payment);
                    balanceShow.setText(String.valueOf(newBalance).concat(" руб"));
                    balanceUpdateText.setText("Успешная оплата.");
                } else {
                    balanceUpdateText.setText("Недостаточно средств.");
                }
            }
        }
        List<Visit> activeSessions = visitRepo.findByUserAndEndTimeIsNotNull(user);
        if (!activeSessions.isEmpty()) {
            Visit activeSession = activeSessions.get(0);
            Time endTime = activeSession.getEndTime();
            String endTimeString = endTime != null ? endTime.toString() : "Error with session";
            dateEndSession.setText("Сессия кончится в " + endTimeString);
        } else {
            dateEndSession.setText("Сессия ещё не куплена.");
        }
    }

    @FXML
    void amountItogAction(ActionEvent event) {
        int hours = Integer.parseInt(hoursInputButton.getText());
        int computerNumber = numberComputerList2.getValue();
        double sessionCost = hours * computerRepo.findByNumber(computerNumber).getCost();
        amountItog.setText(String.format("%.2f руб", sessionCost));
    }

    @FXML
    void hoursInput(ActionEvent event) {

    }

    @FXML
    void gameSelected(ActionEvent event) {

    }

    @FXML
    void goToGames(ActionEvent event) {
        gamesPane.setVisible(true);
        balancePane.setVisible(false);
        userPane.setVisible(true);
        usernameShow.setText(storage.getUsername());
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);
        List<Game> games = gameRepo.findAll();
        List<String> gameNames = games.stream()
                .map(Game::getName)
                .collect(Collectors.toList());
        gameList.setItems(FXCollections.observableArrayList(gameNames));
        List<Integer> computerNumbers = computerRepo.getAllComputerNumbers();
        numberComputerList2.setItems(FXCollections.observableArrayList(computerNumbers));
        List<Visit> activeSessions = visitRepo.findByUserAndEndTimeIsNotNull(user);
        if (!activeSessions.isEmpty()) {
            Visit activeSession = activeSessions.get(0);
            Time endTime = activeSession.getEndTime();
            String endTimeString = endTime != null ? endTime.toString() : "Error with session";
            dateEndSession.setText("Сессия кончится в " + endTimeString);
        } else {
            dateEndSession.setText("Сессия ещё не куплена.");
        }
    }

    @SneakyThrows
    @FXML
    void openGame(ActionEvent event) {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            Game games = gameRepo.findByName(selectedGame);
            ProcessBuilder p = new ProcessBuilder();
            p.command(games.getPath());
            p.start();
        }
    }

    @FXML
    void viewBalance(ActionEvent event) {
        gamesPane.setVisible(false);
        balancePane.setVisible(true);
        userPane.setVisible(true);
        usernameShow.setText(storage.getUsername());
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);
        balanceShow.setText(String.valueOf(user.getBalance()).concat(" руб"));
        List<Integer> computerNumbers = computerRepo.getAllComputerNumbers();
        numberComputerList.setItems(FXCollections.observableArrayList(computerNumbers));
        numberComputerList2.setItems(FXCollections.observableArrayList(computerNumbers));
        List<Visit> activeSessions = visitRepo.findByUserAndEndTimeIsNotNull(user);
        if (!activeSessions.isEmpty()) {
            Visit activeSession = activeSessions.get(0);
            Time endTime = activeSession.getEndTime();
            String endTimeString = endTime != null ? endTime.toString() : "Error with session";
            dateEndSession.setText("Сессия кончится в " + endTimeString);
        } else {
            dateEndSession.setText("Сессия ещё не куплена.");
        }
    }
    @FXML
    void numberComputer(ActionEvent event) {
        User user = userRepo.findByUsername(storage.getUsername());
        Integer selectedComputerNumber = numberComputerList.getValue();
        if (selectedComputerNumber != null) {
            List<Computer> computers = computerRepo.findByNumber(selectedComputerNumber);
            if (!computers.isEmpty()) {
                double costPerHour = computers.get(0).getCost();
                double hours = user.getBalance() / costPerHour;
                amountUser.setText(String.valueOf(hours).concat(" часов"));
            }
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
