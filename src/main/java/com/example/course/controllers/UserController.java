package com.example.course.controllers;

import com.example.course.CourseApplication;
import com.example.course.entities.Computer;
import com.example.course.entities.Game;
import com.example.course.entities.User;
import com.example.course.repo.AdminRepo;
import com.example.course.repo.ComputerRepo;
import com.example.course.repo.GameRepo;
import com.example.course.repo.UserRepo;
import com.example.course.service.Storage;
import javafx.collections.FXCollections;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    private ComboBox<String> gameList;

    @FXML
    private AnchorPane gamesPane;

    @FXML
    private Button goToGamesButton;

    @FXML
    private VBox historyTable;

    @FXML
    private Button logOutButton;

    @FXML
    private ComboBox<Integer> numberComputerList;

    @FXML
    private Button openGameButton;

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

    @FXML
    void gameSelected(ActionEvent event) {

    }

    @FXML
    void goToGames(ActionEvent event) {
        gamesPane.setVisible(true);
        balancePane.setVisible(false);

        List<Game> games = gameRepo.findAll();

        List<String> gameNames = games.stream()
                .map(Game::getName)
                .collect(Collectors.toList());

        gameList.setItems(FXCollections.observableArrayList(gameNames));
    }

    @FXML
    void openGame(ActionEvent event) {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            List<Game> games = gameRepo.findByName(selectedGame);
            if (!games.isEmpty()) {
                String gamePath = games.get(0).getPath();
                File file = new File(gamePath);

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Открытие файла не поддерживается на данной платформе.");
                }
            }
        }
    }

    @FXML
    void viewBalance(ActionEvent event) {
        gamesPane.setVisible(false);
        balancePane.setVisible(true);
        User user = userRepo.findByUsername(storage.getUsername());
        balanceShow.setText(String.valueOf(user.getBalance()).concat(" руб"));
        List<Integer> computerNumbers = computerRepo.getAllComputerNumbers();
        numberComputerList.setItems(FXCollections.observableArrayList(computerNumbers));
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
