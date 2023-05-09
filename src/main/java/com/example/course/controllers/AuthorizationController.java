package com.example.course.controllers;
import com.example.course.CourseApplication;
import com.example.course.enities.Employee;
import com.example.course.repositories.DentistryRepo;
import com.example.course.repositories.EmployeeRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableAutoConfiguration
public class AuthorizationController extends CourseApplication {

    @Autowired
    private DentistryRepo dentistryRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginErrorMsg;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void login(){
        if(employeeRepo.existsByLogin(loginField.getText())) {
            Employee employee = employeeRepo.findByLogin(loginField.getText());
            if(employee.getPassword().equals(passwordField.getText())){
                loginErrorMsg.getScene().getWindow().hide();
                String access = employee.getAccess().getName();
                loginErrorMsg.setVisible(false);
                switch (access) {
                    case ("owner"):
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/ownerScene.fxml"));
                            fxmlLoader.setControllerFactory(springContext::getBean);
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load());
                            stage.setTitle("Здравствуйте, " + employee.getName());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case ("doctor"):
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/doctor.fxml"));
                            fxmlLoader.setControllerFactory(springContext::getBean);
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load());
                            stage.setTitle("Здравствуйте, " + employee.getName());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case ("reception"):
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(CourseApplication.class.getResource("/fxmlScenes/reception.fxml"));
                            fxmlLoader.setControllerFactory(springContext::getBean);
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load());
                            stage.setTitle("Здравствуйте, " + employee.getName());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            } else {
                loginErrorMsg.setVisible(true);
                loginErrorMsg.setText("Ошибка доступа");
            }
        }else {
            loginErrorMsg.setVisible(true);
            loginErrorMsg.setText("Ошибка доступа");
        }
    }

}

