package com.example.course.controllers;

import com.example.course.CourseApplication;
import com.example.course.entities.*;
import com.example.course.repo.*;
import com.example.course.service.Storage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@EnableAutoConfiguration
@Configuration
@EnableScheduling
public class UserControllerOld extends CourseApplication {

    @FXML
    private Text balanceInfoText;

    @FXML
    private Text balanceNowText;

    @FXML
    private Text costComputer;

    @FXML
    private Text error;

    @FXML
    private ComboBox<String> gameList;

    @FXML
    private Text gameSessionText;

    @FXML
    private AnchorPane gamesAP;

    @FXML
    private VBox gamesVBox;

    @FXML
    private Button logOutButton;

    @FXML
    private Button openGameButton;

    @FXML
    private AnchorPane paymentHistoryAP;

    @FXML
    private VBox paymentHistoryVBox;

    @FXML
    private Text sessionActivity;

    @FXML
    private Button update;

    @FXML
    private Text usernameText;

    @FXML
    private AnchorPane visitHistoryAP;

    @FXML
    private VBox visitHistoryVBox;

    @Autowired
    private Storage storage;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ComputerRepo computerRepo;
    @Autowired
    private GameRepo gameRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private VisitRepo visitRepo;

    private boolean status = false;
    @FXML
    void gameSelected(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);
        Integer number = storage.getNumber();
        Computer computer = computerRepo.findByNumber(number);
        if (status == true) {
            status = false;
            computer.setStatus("свободен");
            computerRepo.save(computer);
            sessionActivity.setText("Сессия закончилась.");
            user.getVisit().forEach(o->{
                if(o.getEndTime()==null){
                    o.setEndTime(Time.valueOf(LocalTime.now()));
                }
            });
            userRepo.save(user);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/auth.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        error.getScene().getWindow().hide();
    }

    @FXML
    void openGame(ActionEvent event) throws IOException {
        if(status == true) {
            String selectedGame = gameList.getSelectionModel().getSelectedItem();
            if (selectedGame != null) {
                String username = storage.getUsername();
                User user = userRepo.findByUsername(username);
                Game games = gameRepo.findByName(selectedGame);
                ProcessBuilder p = new ProcessBuilder();
                p.command(games.getPath());
                p.start();
            } else {
                gameSessionText.setText("Выберите игру");
            }
        } else {
            gameSessionText.setText("У вас неактивная сессия.");
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        usernameText.setText(storage.getUsername());
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);

        List<Game> games = gameRepo.findAll();
        List<String> gameNames = games.stream().map(Game::getName).collect(Collectors.toList());
        gameList.setItems(FXCollections.observableArrayList(gameNames));

        balanceNowText.setText(String.valueOf(user.getBalance()).concat(" руб"));

        Integer numbers = storage.getNumber();
        Computer computers = computerRepo.findByNumber(numbers);

        double costPerHour = computers.getCost();
        double hours = user.getBalance() / costPerHour;
        balanceInfoText.setText(String.valueOf(hours).concat(" часов"));

        paymentHistoryVBox.getChildren().clear();
        List<PaymentHistory> paymentHistories = paymentRepo.findByUserOrderByDateDesc(user);
        paymentHistoryVBox.getChildren().clear();
        for (PaymentHistory payment : paymentHistories) {
            Label paymentLabel = new Label("Дата: " + payment.getDate() + " " + payment.getTime() + " " + " Cумма: " + payment.getAmount() + " руб.");
            paymentHistoryVBox.getChildren().add(paymentLabel);
        }

        visitHistoryVBox.getChildren().clear();
        List<Visit> visits = visitRepo.findByUser(user);
        for (Visit visit : visits) {
            String visitInfo = "Дата: " + visit.getDate() + "  " +
                    " Начало посещения: " + visit.getStartTime() + "  " +
                    " Конец посещения: " + visit.getEndTime();
            Label visitLabel = new Label(visitInfo);
            visitHistoryVBox.getChildren().add(visitLabel);
        }

        gamesVBox.getChildren().clear();
        List<Game> gameList = gameRepo.findAll();
        for (Game game : games) {
            String gameInfo = "Название игры: " + game.getName() + "\n" +
                    "Описание: " + game.getDescription() + "\n";
            Label gameLabel = new Label(gameInfo);
            gamesVBox.getChildren().add(gameLabel);
        }


        String costPerHourString = String.valueOf(costPerHour);
        costComputer.setText(costPerHourString);
        if (status == false) {
            if (user.getBalance() > 0 ) {
                Computer computer = computers;
                computer.setStatus("занят");
                sessionActivity.setText("Сессия началась");
                double costPerMinute = computer.getCost() / 60;
                Visit visit = new Visit();
                visit.setDate(LocalDate.now());
                visit.setStartTime(Time.valueOf(LocalTime.now()));
                visit.setUser(user);
                visit.setComputer(computer);
                computerRepo.save(computer);
                visitRepo.save(visit);
                status = true;
            } else {
                sessionActivity.setText("Пополните баланс");
            }
        } else {
            sessionActivity.setText("Сессия активна");
        }
    }

    @Scheduled(fixedDelay = 1000) // Выполняется каждую минуту (60000 миллисекунд)
    public void checkStatusAndDeductBalance() {
        String username = storage.getUsername();
        User user = userRepo.findByUsername(username);
        Integer number = storage.getNumber();
        Computer computers = computerRepo.findByNumber(number);

        if (computers!=null) {
            Computer computer = computers;

            if (status) {
                double costPerMinute = computer.getCost() / 60;
                double remainingBalance = Math.round(user.getBalance() - costPerMinute);

                if (remainingBalance <= 0) {
                    sessionActivity.setText("Сессия закончена, пополните баланс");
                    status = false;
                } else {
                    user.setBalance(remainingBalance);
                    userRepo.save(user);
                }
            }
        }
    }
}