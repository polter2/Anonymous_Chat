package com.example.AnonChat.model;

import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "userData")
public class Users {
    @Id
    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
