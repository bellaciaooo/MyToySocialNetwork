package com.example.lab6.domain;

import com.example.lab6.utils.Constants;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Long> {
    private Long id1;
    private Long id2;
    private LocalDateTime friendsFrom;
    private String status;

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public Prietenie(Long id1, Long id2, LocalDateTime friendsFrom) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendsFrom = friendsFrom;
        this.status = "pending";
    }

    public Prietenie(Long id1, Long id2, LocalDateTime friendsFrom, String status) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendsFrom = friendsFrom;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "idUtilizator1=" + id1 +
                ", idUtilizator2=" + id2 +
                ", friendsFrom=" + friendsFrom.format(Constants.DATE_TIME_FORMATTER) +
                '}';
    }
}
