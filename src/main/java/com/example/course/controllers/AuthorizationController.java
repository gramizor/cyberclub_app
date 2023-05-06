package com.example.course.controllers;
import com.example.course.repositories.DentistryRepo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationController {

    @Autowired
    private DentistryRepo dentistryRepo;
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
        loginErrorMsg.setText("hi");
        System.out.println(dentistryRepo.findByAddress("МГТУ им Баумана").getAddress());
    }

}

