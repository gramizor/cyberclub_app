package com.example.course.controllers;

import com.example.course.enities.*;
import com.example.course.models.FreeRegModel;
import com.example.course.repositories.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ReceptionController {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private RegistrationRepo registrationRepo;
    @FXML
    private DatePicker dateFilter;

    @FXML
    private ComboBox<String> doctorFilter;

    @FXML
    private AnchorPane fieldEditReg;

    @FXML
    private AnchorPane fieldFreeReg;

    @FXML
    private AnchorPane fieldNewPatient;

    @FXML
    private VBox freeRegList;

    @FXML
    private TextField infoDate;

    @FXML
    private TextField infoDoctor;

    @FXML
    private TextField infoSpec;

    @FXML
    private TextField infoTime;

    @FXML
    private TextField newPatientDOBInput;

    @FXML
    private TextField newPatientGenderInput;

    @FXML
    private TextField newPatientMailInput;

    @FXML
    private TextField newPatientNameInput;

    @FXML
    private TextField newPatientNumberInput;

    @FXML
    private TextField patientInput;

    @FXML
    private VBox patientListBox;

    @FXML
    private ComboBox<String> specFilter;

    @FXML
    void addNewPatient(ActionEvent event) {
        Patient patient = new Patient();
        patient.setName(newPatientNameInput.getText());
        patient.setMail(newPatientMailInput.getText());
        patient.setDOB(newPatientDOBInput.getText());
        patient.setGender(newPatientGenderInput.getText());
        patient.setNumber(newPatientNumberInput.getText());
        patientRepo.save(patient);
        newPatientDOBInput.setText(null);
        newPatientGenderInput.setText(null);
        newPatientMailInput.setText(null);
        newPatientNumberInput.setText(null);
        newPatientNameInput.setText(null);
    }

    @FXML
    void addReg(ActionEvent event) {
        if(patientRepo.existsByName(patientInput.getText())){
            Registration registration = new Registration();
            registration.setPatient(patientRepo.findById(Long.valueOf(patientId.getText())).get());
            registration.setStatus("soon");
            registration.setEmployee(employeeRepo.findByName(infoDoctor.getText()));
            registration.setDate(LocalDate.parse(infoDate.getText()));
            registration.setTime(Time.valueOf(infoTime.getText()));
            registrationRepo.save(registration);
            clearInfo();
            findFreeRegFun();
        }
    }

    private void clearInfo(){
        infoTime.setText(null);
        infoDate.setText(null);
        infoSpec.setText(null);
        infoDoctor.setText(null);
        patientId.setText(null);
        patientInput.setText(null);
    }

    @FXML
    void findFreeReg(ActionEvent event) {
        findFreeRegFun();
    }

    private void findFreeRegFun(){
        Employee employee = employeeRepo.findByName(doctorFilter.getValue());
        Specialization specialization = specializationRepo.findByName(specFilter.getValue());
        LocalDate date = dateFilter.getValue();
        if(dateFilter!=null){
            List<Employee> employeeList = employeeRepo.findEmployeesBySchedule_DateOrderBySchedule_TimeAsc(date);
            List<FreeRegModel> freeRegModels = new ArrayList<>();
            for (Employee em : employeeList){
                List<Registration> registrationList = registrationRepo.findByDateAndEmployee_NameOrderByTimeAsc(date, em.getName());
                FreeRegModel freeRegModel = new FreeRegModel();
                freeRegModel.setEmployee(em);
                em.getSchedule().forEach(o->{
                    if(o.getDate().toString().equals(date.toString())){
                        AtomicBoolean addSc = new AtomicBoolean(true);
                        if(!registrationList.isEmpty()){
                            registrationList.forEach(r->{
                                if(o.getTime().toString().equals(r.getTime().toString())){
                                    addSc.set(false);
                                }
                            });
                        }
                        if (addSc.get())freeRegModel.getScheduleList().add(o);
                    }
                });
                freeRegModels.add(freeRegModel);
            }
            printSchedules(freeRegModels);
        }
    }

    @FXML
    void quit(ActionEvent event) {
        freeRegList.getScene().getWindow().hide();
    }
    private void printSchedules(List<FreeRegModel> freeRegModelList){
        freeRegList.getChildren().clear();
        for(FreeRegModel o : freeRegModelList){
            o.getScheduleList().forEach(s->{
                Label label = new Label(s.getDate() + " - " + s.getTime() + " - " + o.getEmployee().getName());
                AnchorPane anchorPane = new AnchorPane();
                AnchorPane.setLeftAnchor(label, 10d);
                AnchorPane.setRightAnchor(label, 10d);
                AnchorPane.setTopAnchor(label, 10d);
                AnchorPane.setBottomAnchor(label, 10d);
                anchorPane.getChildren().add(label);
                anchorPane.setStyle("-fx-border-color: #000000");
                anchorPane.setPrefHeight(30d);
                anchorPane.setOnMouseClicked(mouseEvent -> {
                    infoDoctor.setText(o.getEmployee().getName());
                    infoSpec.setText(o.getEmployee().getSpecialization().getName());
                    infoDate.setText(s.getDate().toString());
                    infoTime.setText(s.getTime().toString());
                });
                freeRegList.getChildren().add(anchorPane);
            });
        }
    }

    @FXML
    void getPatientList(KeyEvent event) {
        List<Patient> patientList = patientRepo.findPatientsByNameContainingIgnoreCase(patientInput.getText());
        printPatients(patientList);
    }

    private void printPatients(List<Patient> patientList){
        patientListBox.getChildren().clear();
        for(Patient o : patientList){
            Label label = new Label(o.getName() + " - " + o.getNumber());
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                patientId.setText(String.valueOf(o.getId()));
                patientInput.setText(o.getName());
            });
            patientListBox.getChildren().add(anchorPane);
        }
    }

    @FXML
    void showEditReg(MouseEvent event) {
        hideFields();
        fieldEditReg.setVisible(true);
        patientManagementInput.setText(null);
    }

    @FXML
    void showPatients(MouseEvent event) {
        hideFields();
        fieldNewPatient.setVisible(true);
    }

    @FXML
    void showReg(MouseEvent event) {
        hideFields();
        fieldFreeReg.setVisible(true);
        regShowFields();
    }

    private void hideFields(){
        fieldEditReg.setVisible(false);
        fieldFreeReg.setVisible(false);
        fieldNewPatient.setVisible(false);
    }
    @FXML
    private Label patientId;

    private void regShowFields(){
        List<Employee> employeeList = employeeRepo.findAllByNameStartingWithOrderByNameAsc("");
        List<Specialization> specializationList = specializationRepo.findAllByNameStartingWith("");
        doctorFilter.setValue(null);
        doctorFilter.getItems().clear();
        doctorFilter.getItems().add(null);
        specFilter.setValue(null);
        specFilter.getItems().clear();
        specFilter.getItems().add(null);
        for (Employee em : employeeList){
            doctorFilter.getItems().add(em.getName());
        }
        for (Specialization sp : specializationList){
            specFilter.getItems().add(sp.getName());
        }
    }
    @FXML
    private VBox patientListBoxManagement;

    @FXML
    private TextField patientManagementInput;
    @FXML
    void inputManagementChanged(KeyEvent event) {
        List<Patient> patientList = patientRepo.findPatientsByNameContainingIgnoreCase(patientManagementInput.getText());
        printManagementPatients(patientList);
    }
    private void printManagementPatients(List<Patient> patientList){
        patientListBoxManagement.getChildren().clear();
        for(Patient o : patientList){
            Label label = new Label(o.getName() + " - " + o.getNumber());
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                patientManagementInput.setText(o.getName());
                patientManagementId.setText(String.valueOf(o.getId()));
            });
            patientListBoxManagement.getChildren().add(anchorPane);
        }
    }
    @FXML
    private Label patientManagementId;
    @FXML
    private VBox patientRegList;
    @FXML
    private TextField managementInfoDate;

    @FXML
    private TextField managementInfoDoctor;

    @FXML
    private Label managementInfoId;

    @FXML
    private TextField managementInfoPatient;

    @FXML
    private TextField managementInfoSpec;

    @FXML
    private TextField managementInfoTime;
    @FXML
    void showPatientManagementList(ActionEvent event) {
        if(patientManagementInput.getText()!=null){
            printRegistrations();
        }
    }

    private void printRegistrations(){
        List<Registration> registrationList = registrationRepo.findByPatient_IdOrderByDateDesc(Long.valueOf(patientManagementId.getText()));
        patientRegList.getChildren().clear();
        for(Registration o : registrationList){
            Label label = new Label(o.getDate() + " - " + o.getTime() + " - " + o.getEmployee().getName() +" ("+ o.getEmployee().getSpecialization().getName()+")" );
            AnchorPane anchorPane = new AnchorPane();
            AnchorPane.setLeftAnchor(label, 10d);
            AnchorPane.setRightAnchor(label, 10d);
            AnchorPane.setTopAnchor(label, 10d);
            AnchorPane.setBottomAnchor(label, 10d);
            anchorPane.getChildren().add(label);
            anchorPane.setStyle("-fx-border-color: #000000");
            anchorPane.setPrefHeight(30d);
            anchorPane.setOnMouseClicked(mouseEvent -> {
                managementInfoDate.setText(o.getDate().toString());
                managementInfoId.setText(String.valueOf(o.getId()));
                managementInfoDoctor.setText(o.getEmployee().getName());
                managementInfoPatient.setText(o.getPatient().getName());
                managementInfoSpec.setText(o.getEmployee().getSpecialization().getName());
                managementInfoTime.setText(o.getTime().toString());
            });
            patientRegList.getChildren().add(anchorPane);
        }
    }
    @FXML
    void deleteReg(ActionEvent event) {
        if(registrationRepo.existsById(Long.valueOf(managementInfoId.getText()))){
            registrationRepo.deleteRegistration(Long.valueOf(managementInfoId.getText()));
            clearManagementFields();
            printRegistrations();
        }
    }
    private void clearManagementFields(){
        managementInfoDate.setText(null);
        managementInfoId.setText(null);
        managementInfoDoctor.setText(null);
        managementInfoPatient.setText(null);
        managementInfoSpec.setText(null);
        managementInfoTime.setText(null);
    }
}
