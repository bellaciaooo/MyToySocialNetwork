package com.example.lab6.domain;

import java.time.LocalDateTime;

public class UtilizatorPrietenieDTO {
    private Long idFriend;
    private String friendFirstName;
    private String friendLastName;
    private LocalDateTime dataRequest;
    private String status;

    public UtilizatorPrietenieDTO(Long idFriend, String friendFirstName, String friendLastName, LocalDateTime dataRequest, String status) {
        this.idFriend = idFriend;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.dataRequest = dataRequest;
        this.status = status;
    }

    public Long getIdFriend() {
        return idFriend;
    }

    public void setIdFriend(Long idFriend) {
        this.idFriend = idFriend;
    }

    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return friendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        this.friendLastName = friendLastName;
    }

    public LocalDateTime getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(LocalDateTime dataRequest) {
        this.dataRequest = dataRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
