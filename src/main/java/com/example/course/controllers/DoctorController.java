package com.example.course.controllers;

import com.example.course.enities.Procedure;
import com.example.course.enities.Reception;
import com.example.course.enities.Registration;
import com.example.course.repositories.ProcedureRepo;
import com.example.course.repositories.ReceptionRepo;
import com.example.course.repositories.RegistrationRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DoctorController {
    @Autowired
    private RegistrationRepo registrationRepo;
    @Autowired
    private ProcedureRepo procedureRepo;
    @Autowired
    private ReceptionRepo receptionRepo;

    @FXML
    private Label registrationId;

    @FXML
    private TextField patientName;

    @FXML
    private VBox procedureListVbox;

    @FXML
    private TextArea receptionDiagnostic;

    @FXML
    private TextArea receptionTreatment;

    @FXML
    private AnchorPane registrationFields;

    @FXML
    private VBox registrationListBox;
    @FXML
    private AnchorPane receptionField;
    @FXML
    private TextArea receptionComplaints;

    @FXML
    void resetRegistrations(ActionEvent event) {
        List<Registration> registrationList = registrationRepo.findByDateOrderByTimeAsc(LocalDate.now());
        registrationListBox.getChildren().clear();
        for(Registration o : registrationList){
            Label label = new Label(o.getDate() + " - " + o.getTime() + " - " + o.getPatient().getName()+" (" + o.getEmployee().getName() + ")");
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                patientName.setText(o.getPatient().getName());
                registrationId.setText(String.valueOf(o.getId()));
                if(receptionRepo.existsByRegistration(o)){
                    Reception reception = receptionRepo.findByRegistration(o);
                    receptionDiagnostic.setText(reception.getDiagnostic());
                    receptionTreatment.setText(reception.getTreatment());
                    receptionComplaints.setText(reception.getComplaints());

                    globalProcedureList.addAll(procedureRepo.findByReception_Registration_Id(Long.valueOf(registrationId.getText())));
                    resetGlobalProcedureListBox();
                }

                swapToReception();
            });
            registrationListBox.getChildren().add(anchorPane);
        }

    }
    public void swapToRegistrations(){
        registrationFields.setVisible(true);
        receptionField.setVisible(false);
        globalProcedureList.clear();
        clearFields();
    }
    private void clearFields(){
        receptionTreatment.setText(null);
        receptionComplaints.setText(null);
        receptionDiagnostic.setText(null);
        procedureListVbox.getChildren().clear();
    }
    public void swapToReception(){
        registrationFields.setVisible(false);
        receptionField.setVisible(true);
        globalProcedureList.clear();
    }
    @FXML
    void backToRegistrations(ActionEvent event) {
        swapToRegistrations();
    }
    @FXML
    private VBox procedureListBox;

    private List<Procedure> globalProcedureList = new ArrayList<>();
    @FXML
    void findProcedure(KeyEvent event) {
        List<Procedure> procedureList = procedureRepo.findByNameContainingIgnoreCase(procedureInput.getText());
        procedureListBox.getChildren().clear();
        for(Procedure o : procedureList){
            Label label = new Label(o.getName() + " - " + o.getCost());
            Button button = new Button();
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            AnchorPane.setRightAnchor(button, 10d);
            AnchorPane.setTopAnchor(button, 10d);
            AnchorPane.setBottomAnchor(button, 10d);
            button.setText("Добавить");
            button.setStyle("-fx-border-color: #5dabd0");
            button.setOnAction(mouseEvent->{
                globalProcedureList.add(o);
                resetGlobalProcedureListBox();
            });
            anchorPane.getChildren().add(button);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            procedureListBox.getChildren().add(anchorPane);
        }
    }

    private void resetGlobalProcedureListBox(){
        procedureListVbox.getChildren().clear();
        for(Procedure o : globalProcedureList){
            Label label = new Label(o.getName() + " - " + o.getCost());
            Button button = new Button();
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            AnchorPane.setRightAnchor(button, 10d);
            AnchorPane.setTopAnchor(button, 10d);
            AnchorPane.setBottomAnchor(button, 10d);
            button.setText("Удалить");
            button.setStyle("-fx-border-color: #5dabd0");
            button.setOnAction(mouseEvent->{
                globalProcedureList.remove(o);
                resetGlobalProcedureListBox();
            });
            anchorPane.getChildren().add(button);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            procedureListVbox.getChildren().add(anchorPane);
        }
    }
    @FXML
    private TextField procedureInput;

    @FXML
    void saveReception(ActionEvent event) {
        Optional<Registration> registrationOpt = registrationRepo.findById(Long.valueOf(registrationId.getText()));
        Registration registration = registrationOpt.get();
        Reception reception = new Reception();
        reception.setComplaints(receptionComplaints.getText());
        reception.setDiagnostic(receptionDiagnostic.getText());
        reception.setTreatment(receptionTreatment.getText());
        reception.setProcedure(globalProcedureList);
        reception.setRegistration(registration);
        registration.setReception(reception);
        registration.setStatus("done");
        registrationRepo.save(registration);
    }
    @FXML
    void quit(ActionEvent event) {
        receptionDiagnostic.getScene().getWindow().hide();
    }

}
