package com.example.sortingalgorithmtesting.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertMessage {

    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
    Alert alert = new Alert(alertType);

    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);

    alert.showAndWait();

    }


    //Stolen from ChatGPT
    public static boolean showConfirmationAlert(String title, String headerText, String contentText) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        return result == ButtonType.OK;
    }
}
