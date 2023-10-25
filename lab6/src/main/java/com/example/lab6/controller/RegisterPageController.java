package com.example.lab6.controller;

import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.validators.ValidationException;
import com.example.lab6.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterPageController {
    private Service service;

    @FXML
    TextField registerFirstName;

    @FXML
    TextField registerLastName;

    @FXML
    TextField registerEmail;

    @FXML
    PasswordField registerPassword;

    @FXML
    Button registerButton;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    public void handleRegisterUser(){
        String firstName = registerFirstName.getText();
        String lastName = registerLastName.getText();
        String email = registerEmail.getText();
        String password = registerPassword.getText();

        if( firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti datele corespunzatoare cerute");
            return;
        }

        try{
            //adaugam utilizatorul in baza de date
            service.saveUser(firstName,lastName,email,password);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","You have been successfully registered!");
            //aici se inchidem fereastra dupa o executie de success
            Stage thisStage = (Stage) registerButton.getScene().getWindow();
            thisStage.close();
        }
        catch (ValidationException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
        catch (IllegalArgumentException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
        catch (RuntimeException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }

}
