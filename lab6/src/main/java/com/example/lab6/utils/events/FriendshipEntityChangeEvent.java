package com.example.lab6.utils.events;

import com.example.lab6.domain.Message;
import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.Utilizator;

public class FriendshipEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Prietenie data, oldData;
    private Message dataM, oldDataM;

    //pentru salvarea mesajelor
    public FriendshipEntityChangeEvent(ChangeEventType type, Message dataM) {
        this.type = type;
        this.dataM = dataM;
    }

    public FriendshipEntityChangeEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipEntityChangeEvent(ChangeEventType type, Prietenie data, Prietenie oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getData() {
        return data;
    }

    public Prietenie getOldData() {
        return oldData;
    }
}
