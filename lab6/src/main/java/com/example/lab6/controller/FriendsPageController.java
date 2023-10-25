package com.example.lab6.controller;

import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.UtilizatorPrietenieDTO;
import com.example.lab6.service.Service;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.events.UserEntityChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsPageController implements Observer<FriendshipEntityChangeEvent> {
    private Service service;

    private Utilizator user;

    ObservableList<Utilizator> usersModel = FXCollections.observableArrayList();

    @FXML
    Button addFriendButton;

    @FXML
    TextField searchUserName;

    @FXML
    TableView<Utilizator> usersList;

    @FXML
    TableColumn<Utilizator,String> firstNameColumn;

    @FXML
    TableColumn<Utilizator,String> lastNameColumn;

    @FXML
    TableColumn<Utilizator,String> emailColumn;

    public void setService(Service service){
        this.service = service;
        service.addObserver(this);
    }

    public void setUser(Utilizator utilizator) {
        this.user = utilizator;
        initModel();
    }

    private void initModel(){
        Iterable<Utilizator> messages = service.getNotFriends(user.getId());
        List<Utilizator> notFriends = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(notFriends.toArray().length);
        usersModel.setAll(notFriends);

        searchUserName.textProperty().addListener(o -> handleSearchByName());
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("email"));

        usersList.setItems(usersModel);

        //usersList.setBackground(Color.GREEN.);

    }

    public void handleSearchByName(){
        Predicate<Utilizator> pName = n -> n.getLastName().startsWith(searchUserName.getText());
        usersModel.setAll(service.getNotFriends(user.getId())
                .stream()
                .filter(pName)
                .collect(Collectors.toList()));
    }

    @FXML
    public void handleAddFriend(ActionEvent actionEvent){
        Utilizator friend = (Utilizator) usersList.getSelectionModel().getSelectedItem();
        if(friend != null) {
            if(service.findFriendship(user.getId(),friend.getId()) == null){
                service.addFriendship(user.getId(),friend.getId());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Your friend request for this user was successfully sent!");
            }
            else
            {   //inseamna ca exista prietenia intr un anumit status
                Prietenie prietenie = service.findFriendship(user.getId(),friend.getId());
                if(Objects.equals(prietenie.getStatus(), "pending")){
                    if(!Objects.equals(prietenie.getId1(), user.getId())) {
                        //inseamna ca el ne-a dat cerere de prietenie si se accepta automat
                        service.updateFriendship(friend.getId(), user.getId(), LocalDateTime.now(), "accepted");
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "You and the user are friends now!");
                    }
                    else
                    {  //inseamna ca noi mai incercam inca o data sa trimitem cererea de prietenie
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Your friend request for this user is still in pending!");
                    }
                }
                else if(Objects.equals(prietenie.getStatus(), "deleted")){
                    //cand a fost trimisa cererea, mai apoi stearsa, iar acum o trimitem noi din nou
                    service.removeFriendship(friend.getId(), user.getId());
                    service.addFriendship(user.getId(), friend.getId());
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Your friend request for this user was successfully sent!");
                }
            }
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Select a friend so you can add a new friendship!");
        initModel();
    }

    @FXML
    public void handleCancelFriendRequest(){
        Utilizator utilizator = (Utilizator) usersList.getSelectionModel().getSelectedItem();
        if(utilizator != null) {
            Prietenie friendRequest = service.findFriendship(user.getId(), utilizator.getId());
            if(friendRequest != null && Objects.equals(friendRequest.getId1(), user.getId())
                    && Objects.equals(friendRequest.getStatus(), "pending"))
            {
                //stergem cererea de prietenie existenta
                service.removeFriendship(user.getId(), utilizator.getId());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request successfully deleted!");
            }
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Doesn't exist friend request for this user!");

        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Select a user to cancel friend request!");
        initModel();
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
}
