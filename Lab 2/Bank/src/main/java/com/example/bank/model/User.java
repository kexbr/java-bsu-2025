package com.example.bank.model;

import java.util.List;
import java.util.UUID;

public class User {
    private final UUID userId;
    private String nickname;
    private List<String> accountIds; // Список ID счетов пользователя

    public User(String nickname, List<String> accountIds) {
        this.userId = UUID.randomUUID();
        this.nickname = nickname;
        this.accountIds = accountIds;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getNickname() {
        return nickname;
    }
    public List<String> getAccountIds() {
        return accountIds;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAccountIds(List<String> accountIds) {
        this.accountIds = accountIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", accountIds=" + accountIds +
                '}';
    }
}