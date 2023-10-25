package com.example.lab6.controller;

import com.example.lab6.domain.Message;
import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.UtilizatorPrietenieDTO;
import com.example.lab6.service.Service;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MessagePageController implements Observer<FriendshipEntityChangeEvent> {
    private Service service;
    private Utilizator me;
    private Utilizator userMessage;

    @FXML
    Button sendMessageButton;

    @FXML
    TextField mesaj;

    @FXML
    private VBox messages;

    @FXML
    private ScrollPane scrollPane;

    public void setService(Service service){
        this.service = service;
        service.addObserver(this);
    }

    public void setMeUser(Utilizator me,Utilizator utilizator) {
        this.me = me;
        this.userMessage = utilizator;
        initModel();
    }

    public void initModel() {
        this.messages.getChildren().clear();
        List<Message> mesaje = this.service.getMyMessagesWithUser(me.getId(),userMessage.getId());

        if(mesaje.size() > 0) {
            for(Message element : mesaje){
                String mesaj = element.getContent();

                if (!mesaj.isEmpty()) {
                    HBox hBox = new HBox();
                    if (Objects.equals(element.getSenderId(), me.getId()))
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                    else
                        hBox.setAlignment(Pos.CENTER_LEFT);

                    Text text = new Text(mesaj);

                    hBox.getChildren().add(text);
                    messages.getChildren().add(hBox);
                }

            }
        }
    }

    @FXML
    void handleSendButton() {
        String message = this.mesaj.getText();
        if (!message.isEmpty()) {
            service.saveMessage(me.getId(), userMessage.getId(), message);

            initModel();
            /*HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            Text text = new Text(message);

            hBox.getChildren().add(text);
            messages.getChildren().add(hBox);*/
            this.mesaj.clear();
        }
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
}
