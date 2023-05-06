package com.example.course.controllers;
import com.example.course.enities.Access;
import com.example.course.enities.Dentistry;
import com.example.course.enities.Employee;
import com.example.course.repositories.AccessRepo;
import com.example.course.repositories.DentistryRepo;
import com.example.course.repositories.EmployeeRepo;
import jakarta.transaction.Transactional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OwnerController {

    @Autowired
    private DentistryRepo dentistryRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AccessRepo accessRepo;

    @FXML
    private VBox boxOfEmployees;

    @FXML
    private ComboBox<String> dentistryListComboBox;

    @FXML
    private AnchorPane employeeManagementButton;

    @FXML
    private AnchorPane employeeManagementField;

    @FXML
    private AnchorPane hello;

    @FXML
    private AnchorPane helloButton;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField jobTitleInput;
    @FXML
    private TextField loginInput;

    @FXML
    private TextField numberInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private AnchorPane mainField;

    @FXML
    private Label username;
    @FXML
    private AnchorPane employeeInfoField;
    @FXML
    private Button deleteEmployeeButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    private Label employeeId;
    @FXML
    private ComboBox<String> accessListComboBox;

    @FXML
    private Button addNewEmployeeButton;

    @FXML
    void dentistryChanged(ActionEvent event) {
        dentistryChangedFun();
    }
    void dentistryChangedFun() {
        clearEmployeeInfoField();
        boxOfEmployees.getChildren().clear();
        String address = dentistryListComboBox.getValue();
        Dentistry dentistry = dentistryRepo.findByAddress(address);
        List<Employee> employees = employeeRepo.findAllByDentistryOrderByNameAsc(dentistry);
        for(Employee employee : employees){
            Label label = new Label(employee.getName() + " - " + employee.getJobTitle());
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                employeeInfoField.setVisible(true);
                nameInput.setText(employee.getName());
                jobTitleInput.setText(employee.getJobTitle());
                numberInput.setText(employee.getNumber());
                loginInput.setText(employee.getLogin());
                passwordInput.setText(employee.getPassword());
                employeeId.setText(String.valueOf(employee.getId()));
                if(employee.getAccess()!=null) {
                    accessListComboBox.setValue(employee.getAccess().getName());
                }
            });
            boxOfEmployees.getChildren().add(anchorPane);
        }
    }

    public void clearEmployeeInfoField(){
        accessListComboBox.getItems().clear();
        accessListComboBox.getItems().addAll("owner", "reception", "doctor");
        nameInput.setText(null);
        jobTitleInput.setText(null);
        numberInput.setText(null);
        loginInput.setText(null);
        passwordInput.setText(null);
        employeeId.setText(null);
        accessListComboBox.setValue(null);
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if(employeeId.getText() != null) {
            Optional<Employee> employee = employeeRepo.findById(Integer.valueOf(employeeId.getText()));
            employee.map(o -> {
                o.setName(nameInput.getText());
                o.setJobTitle(jobTitleInput.getText());
                o.setNumber(numberInput.getText());
                o.setLogin(loginInput.getText());
                o.setPassword(passwordInput.getText());
                Access access = accessRepo.findByName(accessListComboBox.getValue());
                o.setAccess(access);
                employeeRepo.save(o);
                return null;
            });
        }else{
            Employee em = new Employee();
            String address = dentistryListComboBox.getValue();
            Dentistry dentistry = dentistryRepo.findByAddress(address);
            em.setDentistry(dentistry);
            em.setName(nameInput.getText());
            em.setJobTitle(jobTitleInput.getText());
            em.setNumber(numberInput.getText());
            em.setLogin(loginInput.getText());
            em.setPassword(passwordInput.getText());
            Access access = accessRepo.findByName(accessListComboBox.getValue());
            em.setAccess(access);
            employeeRepo.save(em);
        }
        dentistryChangedFun();
    }

    private void hide(){
        employeeManagementField.setVisible(false);
        hello.setVisible(false);
    }

    @FXML
    void showEmployeeManagement(MouseEvent event) {
        hide();
        employeeManagementField.setVisible(true);
        clearEmployeeInfoField();
        boxOfEmployees.getChildren().clear();
        List<Dentistry> listOfDentistry = dentistryRepo.findAllByAddressIsStartingWith("");
        if(!listOfDentistry.isEmpty()) {
            dentistryListComboBox.getItems().clear();
            dentistryListComboBox.setValue(listOfDentistry.get(0).getAddress());
            for (Dentistry dentistry : listOfDentistry) {
                dentistryListComboBox.getItems().add(dentistry.getAddress());
            }
        }
        dentistryChangedFun();
    }

    @FXML
    void addNewEmployee(ActionEvent event) {
        clearEmployeeInfoField();
        dentistryChanged(event);
    }
    @FXML
    void deleteEmployee(ActionEvent event) {
        System.out.println(employeeId.getText());
        if(employeeId.getText() != null) {
            employeeRepo.deleteEmployee((Integer.valueOf(employeeId.getText())));
        }
        dentistryChangedFun();
    }

    @FXML
    void addBox(ActionEvent event) {

    }

    @FXML
    void showHello(MouseEvent event) {
        hide();
        hello.setVisible(true);
    }

}
