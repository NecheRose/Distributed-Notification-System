package com.notification.hnguser.dto;

import lombok.Data;

import java.util.Map;

public class UserRequest {
        private String name;
        private String email;
        private String push_token;
        private Map<String, Boolean> preferences;
        private String password;


    public UserRequest() {
    }

    public UserRequest(String name, String email, String push_token, Map<String, Boolean> preferences, String password) {
        this.name = name;
        this.email = email;
        this.push_token = push_token;
        this.preferences = preferences;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public Map<String, Boolean> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Boolean> preferences) {
        this.preferences = preferences;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "UserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", push_token='" + push_token + '\'' +
                ", preferences=" + preferences +
                ", password='" + password + '\'' +
                '}';
    }
}





