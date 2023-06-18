package com.example.course.controllers;
import com.example.course.CourseApplication;
import com.example.course.entities.Admin;
import com.example.course.entities.User;
import com.example.course.repo.AdminRepo;
import com.example.course.repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;


@Component
@EnableAutoConfiguration
public class AuthorizationController extends CourseApplication {

    @FXML
    private Button authButton;

    @FXML
    private AnchorPane authorizationPane;

    @FXML
    private Button regButton;

    @FXML
    private AnchorPane registrationPane;
    @FXML
    private TextField loginRegInput;

    @FXML
    private TextField mailRegInput;

    @FXML
    private PasswordField passwordRegInput;
    @FXML
    private TextField loginAuthInput;
    @FXML
    private PasswordField passwordAuthInput;
    @FXML
    private Text error;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdminRepo adminRepo;

    @SneakyThrows
    @FXML
    void authorization(ActionEvent event) {
        if(!userRepo.existsByUsername(loginAuthInput.getText()) && !adminRepo.existsByUsername(loginAuthInput.getText())){
            error.setText("Пользователя нет");
        }else{
            User user = new User();
            user = userRepo.findByUsername(loginAuthInput.getText());
            Admin admin = new Admin();
            admin = adminRepo.findByUsername(loginAuthInput.getText());
            if(!user.getPassword().equals(passwordAuthInput.getText())){
                error.setText("Неверный пароль");
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/user.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
                error.getScene().getWindow().hide();
            }
            if(!admin.getPassword().equals(passwordAuthInput.getText())){
                error.setText("Неверный пароль");
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/admin.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
                error.getScene().getWindow().hide();
            }
        }
    }

    @FXML
    void goToAuthorization(ActionEvent event) {
        regButton.setVisible(true);
        registrationPane.setVisible(false);
        authButton.setVisible(false);
        authorizationPane.setVisible(true);
    }

    @FXML
    void goToRegistration(ActionEvent event) {
        regButton.setVisible(false);
        registrationPane.setVisible(true);
        authButton.setVisible(true);
        authorizationPane.setVisible(false);
    }

    @FXML
    void registration(ActionEvent event) {
        User user = new User();
        user.setMail(mailRegInput.getText());
        user.setUsername(loginRegInput.getText());
        user.setPassword(passwordRegInput.getText());
        userRepo.save(user);
    }
}

