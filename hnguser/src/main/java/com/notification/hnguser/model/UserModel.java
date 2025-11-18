package com.notification.hnguser.model;


import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "users")


public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "push_token")
    private String pushToken;

    @Column(nullable = false)
    private String password;

    @Column(name = "email_enabled")
    private boolean emailEnabled = true;

    @Column(name = "push_enabled")
    private boolean pushEnabled = true;


    public UserModel() {

    }


    public UserModel(Long userId, String name, String email, String pushToken, String password, boolean emailEnabled, boolean pushEnabled) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.pushToken = pushToken;
        this.password = password;
        this.emailEnabled = emailEnabled;
        this.pushEnabled = pushEnabled;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pushToken='" + pushToken + '\'' +
                ", password='" + password + '\'' +
                ", emailEnabled=" + emailEnabled +
                ", pushEnabled=" + pushEnabled +
                '}';
    }
}