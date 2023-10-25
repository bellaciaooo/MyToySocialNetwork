package com.example.lab6.controller;

import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.validators.ValidationException;
import com.example.lab6.service.Service;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController implements Observer<FriendshipEntityChangeEvent> {
    private Service service;

    @FXML
    TextField loginEmail;

    @FXML
    PasswordField loginPassword;

    @FXML
    Button loginButton;

    @FXML
    Button registerButton;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    void loginButtonClicked() throws IOException {
        String email = loginEmail.getText();
        String password = loginPassword.getText();

        if( loginEmail.getText().isEmpty() || loginPassword.getText().isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti datele corespunzatoare");
            return;
        }

        try {
            Utilizator utilizator = service.loginUser(email, password);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/homePage.fxml"));
            AnchorPane root = loader.load();

            HomePageController homePageController = loader.getController();
            homePageController.setService(service);
            homePageController.setUser(utilizator);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 554, 428));
            stage.setTitle("Hello, " + utilizator.getFirstName()+"!");
            stage.show();

            //aici se inchide fereastra
            Stage thisStage = (Stage) loginButton.getScene().getWindow();
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

    @FXML
    void registerButtonClicked() throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/registerPage.fxml"));
            AnchorPane root = loader.load();

            RegisterPageController registerPageController = loader.getController();
            registerPageController.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 432, 377));
            stage.setTitle("You can register here!");
            stage.show();
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

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
    }
}
