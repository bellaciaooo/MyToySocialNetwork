package com.example.lab6.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime sendingDate;

    public Message(Long senderId, Long receiverId, String content, LocalDateTime sendingDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sendingDate = sendingDate;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(LocalDateTime sendingDate) {
        this.sendingDate = sendingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(senderId, message.senderId) && Objects.equals(receiverId, message.receiverId) && Objects.equals(content, message.content) && Objects.equals(sendingDate, message.sendingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, content, sendingDate);
    }
}
