package com.example.lab6.controller;

import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.validators.ValidationException;
import com.example.lab6.service.Service;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.events.UserEntityChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomePageController implements Observer<FriendshipEntityChangeEvent> {
    private Service service;

    private Utilizator user;

    ObservableList<Utilizator> usersModel = FXCollections.observableArrayList();

    @FXML
    Button logoutButton;

    @FXML
    Button removeFriendButton;

    @FXML
    Button addNewFriendButton;

    @FXML
    Button friendRequestsButton;

    @FXML
    Button sentRequestButton;

    @FXML
    Button sendMessageButton;

    @FXML
    Button newLoginButton;

    @FXML
    TableColumn<Utilizator,String> firstNameColumn;

    @FXML
    TableColumn<Utilizator,String> lastNameColumn;

    @FXML
    TableColumn<Utilizator,String> emailColumn;

    @FXML
    TableView<Utilizator> friendsList;

    public void setService(Service service){
        this.service = service;
        service.addObserver(this);
    }

    public void setUser(Utilizator utilizator) {
        this.user = utilizator;
        initModel();

    }

    private void initModel(){
        Iterable<Utilizator> messages = service.friendList(user.getId());
        List<Utilizator> friends = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(friends.toArray().length);
        usersModel.setAll(friends);
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("email"));

        friendsList.setItems(usersModel);

    }

    @FXML
    void friendRequestsButtonClicked() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/friendRequestPage.fxml"));
            AnchorPane root = loader.load();

            FriendRequestPageController friendRequestPageController = loader.getController();
            friendRequestPageController.setService(service);
            friendRequestPageController.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 531, 372));
            stage.setTitle("Here are your requests to magical friendships!");
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

    @FXML
    void sentRequestButtonClicked() throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/sentRequestPage.fxml"));
            AnchorPane root = loader.load();

            SentRequestPageController sentRequestPageController = loader.getController();
            sentRequestPageController.setService(service);
            sentRequestPageController.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 531, 372));
            stage.setTitle("Here are your sent requests in pending!");
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

    @FXML
    void sendMessageButtonClicked() throws IOException {
        Utilizator friend = (Utilizator) friendsList.getSelectionModel().getSelectedItem();
        if(friend != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/lab6/messagesPage.fxml"));
                AnchorPane root = loader.load();

                MessagePageController messagePageController = loader.getController();
                messagePageController.setService(service);
                messagePageController.setMeUser(user, friend);

                Stage stage = new Stage();
                stage.setScene(new Scene(root, 495, 385));
                stage.setTitle("Here is your conversation with "+friend.getFirstName()+"!");
                stage.show();

            } catch (ValidationException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
                return;
            } catch (RuntimeException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
                return;
            }
        }
        else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Select a friend so you can send a message!");
         }
    }

    @FXML
    void addNewFriendButtonClicked() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/friendsPage.fxml"));
            AnchorPane root = loader.load();

            FriendsPageController friendsPageController = loader.getController();
            friendsPageController.setService(service);
            friendsPageController.setUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 463, 423));
            stage.setTitle("Here you can select who can be your new friend!");
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

    @FXML
    public void handleRemoveFriend(ActionEvent actionEvent){
        Utilizator friend = (Utilizator) friendsList.getSelectionModel().getSelectedItem();
        if(friend != null)
        {
            service.updateFriendship(user.getId(),friend.getId(), LocalDateTime.now(),"deleted");
            initModel();
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Select a friend so you can remove the friendship!");
    }

    @FXML
    public void logoutButtonClicked() throws IOException{
        try {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Goodbye friend!", "See you soon,"+user.getFirstName()+" "+
            user.getLastName()+"!");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/lab6/loginPage.fxml"));
            AnchorPane root = loader.load();

            LoginPageController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            Scene scene = new Scene(root, 432, 377);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();

            //aici se inchide fereastra de homepage
            Stage thisStage = (Stage) logoutButton.getScene().getWindow();
            thisStage.close();

        } catch (ValidationException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (RuntimeException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }

    @FXML
    void newLoginButtonClicked() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        //System.out.println(getClass().getResource("loginPage.fxml"));
        loader.setLocation(getClass().getResource("/com/example/lab6/loginPage.fxml"));
        AnchorPane root = loader.load();

        LoginPageController controller = loader.getController();
        controller.setService(service);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 432, 377);
        stage.setTitle("Social Network");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
}
