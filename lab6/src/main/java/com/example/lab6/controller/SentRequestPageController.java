package com.example.lab6.controller;

import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.UtilizatorPrietenieDTO;
import com.example.lab6.service.Service;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SentRequestPageController implements Observer<FriendshipEntityChangeEvent> {
    private Service service;

    private Utilizator user;

    ObservableList<UtilizatorPrietenieDTO> requestsModel = FXCollections.observableArrayList();

    @FXML
    TableColumn<UtilizatorPrietenieDTO,String> firstNameColumn;

    @FXML
    TableColumn<UtilizatorPrietenieDTO,String> lastNameColumn;

    @FXML
    TableColumn<UtilizatorPrietenieDTO,String> statusColumn;

    @FXML
    TableColumn<UtilizatorPrietenieDTO, LocalDateTime> dataColumn;

    @FXML
    TableView<UtilizatorPrietenieDTO> RequestsList;

    @FXML
    Button cancelFriendRequest;

    public void setService(Service service){
        this.service = service;
        service.addObserver(this);
    }

    public void setUser(Utilizator utilizator) {
        this.user = utilizator;
        initModel();
    }

    private void initModel(){
        Iterable<UtilizatorPrietenieDTO> messages = service.getSentRequests(user.getId());
        List<UtilizatorPrietenieDTO> friends = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(friends.toArray().length);
        requestsModel.setAll(friends);
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<UtilizatorPrietenieDTO,String>("friendFirstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<UtilizatorPrietenieDTO,String>("friendLastName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<UtilizatorPrietenieDTO,String>("status"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<UtilizatorPrietenieDTO,LocalDateTime>("dataRequest"));

        RequestsList.setItems(requestsModel);
    }

    @FXML
    public void handleCancelFriendRequest(){
        UtilizatorPrietenieDTO request = (UtilizatorPrietenieDTO) RequestsList.getSelectionModel().getSelectedItem();
        if(request != null) {
            //stergem cererea de prietenie existenta in pending
            service.removeFriendship(user.getId(),request.getIdFriend());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request successfully deleted!");
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Select a request to cancel it!");
        initModel();
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent){
        initModel();
    }
}
