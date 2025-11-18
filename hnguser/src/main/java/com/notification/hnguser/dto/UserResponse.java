package com.notification.hnguser.dto;

import com.notification.hnguser.model.UserModel;

public class UserResponse {

    private final String id;
    private final String email;
    private final String name;
    private final String pushToken;
    private final Preferences preferences;

    public UserResponse(String id, String email, String name, String pushToken, Preferences preferences) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pushToken = pushToken;
        this.preferences = preferences;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPushToken() {
        return pushToken;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public static UserResponse from(UserModel user) {
        return new UserResponse(
                user.getUserId() == null ? null : String.valueOf(user.getUserId()),
                user.getEmail(),
                user.getName(),
                user.getPushToken(),
                new Preferences(user.isEmailEnabled(), user.isPushEnabled())
        );
    }

    public static class Preferences {
        private final boolean email;
        private final boolean push;

        public Preferences(boolean email, boolean push) {
            this.email = email;
            this.push = push;
        }

        public boolean isEmail() {
            return email;
        }

        public boolean isPush() {
            return push;
        }
    }
}

