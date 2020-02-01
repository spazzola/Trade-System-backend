package com.tradesystem.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int id;
    private String login;
    private String password;


    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }
}
