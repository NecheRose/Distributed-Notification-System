package com.notification.hnguser.service;

import com.notification.hnguser.model.UserModel;
import com.notification.hnguser.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Create new user method
    @CacheEvict(value = {"users_all", "users"}, allEntries = true)
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    // Fetch all users
    @Cacheable(value = "users_all")
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch single user by ID (caching disabled)
    public Optional<UserModel> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // Update full user info (name, email, password, etc.)
    @CacheEvict(value = {"users", "user_preferences", "users_all"}, key = "#userId", allEntries = true)
    public Optional<UserModel> updateUser(Long userId, UserModel updatedUser) {
        return userRepository.findById(userId).map(existingUser -> {
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getPushToken() != null) {
                existingUser.setPushToken(updatedUser.getPushToken());
            }

            // update preferences if present
            existingUser.setEmailEnabled(updatedUser.isEmailEnabled());
            existingUser.setPushEnabled(updatedUser.isPushEnabled());

            return userRepository.save(existingUser);
        });
    }

    @Cacheable(value = "user_preferences", key = "#userId")
    public Optional<Map<String, Boolean>> getPreferences(Long userId) {
        return userRepository.findById(userId)
                .map(user -> Map.of(
                        "email_enabled", user.isEmailEnabled(),
                        "push_enabled", user.isPushEnabled()
                ));
    }



    // Update preferences by userId
    @CacheEvict(value = {"user_preferences", "users"}, key = "#userId")
    public Optional<UserModel> updatePreferences(Long userId, boolean emailEnabled, boolean pushEnabled) {
        return userRepository.findById(userId).map(user -> {
            user.setEmailEnabled(emailEnabled);
            user.setPushEnabled(pushEnabled);
            return userRepository.save(user);
        });
    }


    //Get push token
    @Cacheable(value = "push_tokens", key = "#userId", unless = "#result == null or #result.isEmpty()")
    public Optional<String> getPushToken(Long userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        String token = user.get().getPushToken();
        return token != null ? Optional.of(token) : Optional.empty();
    }

    // Update push token
    @CacheEvict(value = {"push_tokens", "users"}, key = "#userId")
    public Optional<UserModel> updatePushToken(Long userId, String pushToken) {
        return userRepository.findById(userId).map(user -> {
            user.setPushToken(pushToken);
            return userRepository.save(user);
        });
    }

    //Delete push token
    @CacheEvict(value = {"push_tokens", "users"}, key = "#userId")
    public Optional<UserModel> deletePushToken(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setPushToken(null);
            return userRepository.save(user);
        });
    }
}
