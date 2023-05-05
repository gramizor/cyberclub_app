package com.example.kyrsach.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChartController {

    @FXML
    private Label id;
    public void yes(){
        id.setText("Оправдано");
    }

    public void no(){
        id.setText("Получается ты пидор");
    }
}
