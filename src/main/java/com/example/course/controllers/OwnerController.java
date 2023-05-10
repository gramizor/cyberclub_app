package com.example.course.controllers;
import com.example.course.enities.*;
import com.example.course.repositories.*;
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

    @Autowired
    private ProcedureRepo procedureRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @FXML
    private ComboBox<String> accessListComboBox;

    @FXML
    private VBox boxOfProcedures;
    @FXML
    private TextField procedureCostInput;

    @FXML
    private AnchorPane procedureField;

    @FXML
    private AnchorPane procedureInfoField;

    @FXML
    private AnchorPane procedureManagementButton;

    @FXML
    private TextField procedureNameInput;

    @FXML
    private Button deleteProcedureButton;

    @FXML
    private Button addNewEmployeeButton;

    @FXML
    private VBox boxOfEmployees;

    @FXML
    private Button deleteEmployeeButton;

    @FXML
    private ComboBox<String> dentistryListComboBox;

    @FXML
    private Label employeeId;

    @FXML
    private AnchorPane employeeInfoField;

    @FXML
    private AnchorPane employeeManagementButton;

    @FXML
    private AnchorPane employeeManagementField;

    @FXML
    private TextField jobTitleInput;

    @FXML
    private TextField loginInput;

    @FXML
    private AnchorPane mainField;

    @FXML
    private TextField nameInput;


    @FXML
    private TextField numberInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button saveChangesButton;


    @FXML
    private TextField successDeleteInput;
    @FXML
    private Button showDeleteEmployeeButton;
    @FXML
    private ComboBox<String> specializationListComboBox;


    @FXML
    void dentistryChanged(ActionEvent event) {
        dentistryChangedFun();
    }
    void dentistryChangedFun() {
        clearEmployeeInfoField();
        deleteEmployeeButton.setVisible(false);
        successDeleteInput.setVisible(false);
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
                if(employee.getSpecialization()!= null){
                    specializationListComboBox.setValue(employee.getSpecialization().getName());
                }
                deleteEmployeeButton.setVisible(false);
                successDeleteInput.setVisible(false);
            });
            boxOfEmployees.getChildren().add(anchorPane);
        }
    }

    public void clearEmployeeInfoField(){
        accessListComboBox.getItems().clear();
        specializationListComboBox.getItems().clear();
        List<Access> accessList = accessRepo.findAllByNameStartingWith("");
        List<Specialization> specializationList = specializationRepo.findAllByNameStartingWith("");
        for (Access o : accessList){
            accessListComboBox.getItems().add(o.getName());
        }
        for(Specialization o : specializationList){
            specializationListComboBox.getItems().add(o.getName());
        }
        nameInput.setText(null);
        jobTitleInput.setText(null);
        numberInput.setText(null);
        loginInput.setText(null);
        passwordInput.setText(null);
        employeeId.setText(null);
        specializationListComboBox.setValue(null);
        accessListComboBox.setValue(null);
    }

    @FXML
    void saveChanges(ActionEvent event) {
        saveChangesButton.setText("Сохранить изменения");
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
                Specialization specialization = specializationRepo.findByName(specializationListComboBox.getValue());
                o.setSpecialization(specialization);
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
        procedureField.setVisible(false);
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
        saveChangesButton.setText("Добавить");
        dentistryChanged(event);
    }
    @FXML
    void deleteEmployee(ActionEvent event) {
        if(successDeleteInput.getText().equals("Подтвердить") || successDeleteInput.getText().equals("подтвердить")) {
            if (employeeId.getText() != null) {
                employeeRepo.deleteEmployee((Integer.valueOf(employeeId.getText())));
            }
            dentistryChangedFun();
            successDeleteInput.setText(null);
        }
    }

    @FXML
    void showDeleteEmployee(ActionEvent event) {
        deleteEmployeeButton.setVisible(true);
        successDeleteInput.setVisible(true);
    }
    @FXML
    private Label procedureId;

    @FXML
    void showDeleteProcedure(ActionEvent event) {
        deleteProcedureButton.setVisible(true);
        successProcedureDeleteInput.setVisible(true);
    }

    @FXML
    private Button saveProcedureChangesButton;

    @FXML
    void saveProcedureChanges(ActionEvent event) {
        saveProcedureChangesButton.setText("Сохранить изменения");
        if (procedureId.getText() != null){
            Optional<Procedure> procedureOptional = procedureRepo.findById(Long.valueOf(procedureId.getText()));
            procedureOptional.map(o->{
                o.setName(procedureNameInput.getText());
                o.setCost(Double.parseDouble(procedureCostInput.getText()));
                procedureRepo.save(o);
               return null;
            });
        }else{
            Procedure procedure = new Procedure();
            procedure.setName(procedureNameInput.getText());
            procedure.setCost(Double.parseDouble(procedureCostInput.getText()));
            procedureRepo.save(procedure);
        }
        showProcedureFun();
    }
    @FXML
    void deleteProcedure(ActionEvent event) {
        if(successProcedureDeleteInput.getText().equals("Подтвердить") || successProcedureDeleteInput.getText().equals("подтвердить")) {
            if (procedureId.getText() != null) {
                procedureRepo.deleteProcedure(Long.valueOf(procedureId.getText()));
            }
            showProcedureFun();
            successProcedureDeleteInput.setText(null);
        }
    }
    @FXML
    void addNewProcedure(ActionEvent event) {
        showProcedureFun();
        saveProcedureChangesButton.setText("Добавить");
    }
    @FXML
    private TextField successProcedureDeleteInput;

    @FXML
    void showProcedure(MouseEvent event) {
        hide();
        procedureField.setVisible(true);
        showProcedureFun();
    }

    private void showProcedureFun(){
        successProcedureDeleteInput.setVisible(false);
        deleteProcedureButton.setVisible(false);
        procedureId.setText(null);
        procedureCostInput.setText(null);
        procedureNameInput.setText(null);
        List<Procedure> procedureList = procedureRepo.findAllByNameContainsOrderByNameAsc("");
        boxOfProcedures.getChildren().clear();
        for(Procedure o : procedureList){
            Label label = new Label(o.getName() + " - " + o.getCost());
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                procedureNameInput.setText(o.getName());
                procedureCostInput.setText(String.valueOf(o.getCost()));
                procedureId.setText(String.valueOf(o.getId()));
                successProcedureDeleteInput.setVisible(false);
                deleteProcedureButton.setVisible(false);
            });
            boxOfProcedures.getChildren().add(anchorPane);
        }
    }

    @FXML
    void quit(ActionEvent event) {
        boxOfProcedures.getScene().getWindow().hide();
    }

}
