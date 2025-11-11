package com.notification.hnguser.controller;

import com.notification.hnguser.dto.ApiResponse;
import com.notification.hnguser.dto.PaginationMeta;
import com.notification.hnguser.dto.UserRequest;
import com.notification.hnguser.model.UserModel;
import com.notification.hnguser.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "User Service is running",
                null,
                "Health check successful",
                null
        );
        return ResponseEntity.ok(response);
    }

    // Create user
    @PostMapping
    public ResponseEntity<ApiResponse<UserModel>> createUser(@RequestBody UserRequest userRequest) {
        UserModel newUser = new UserModel();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        newUser.setPushToken(userRequest.getPush_token());

        // Handle preferences safely
        if (userRequest.getPreferences() != null) {
            newUser.setEmailEnabled(userRequest.getPreferences().getOrDefault("email", true));
            newUser.setPushEnabled(userRequest.getPreferences().getOrDefault("push", true));
        }

        UserModel savedUser = userService.createUser(newUser);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                savedUser,
                null,
                "User created successfully",
                null
        ));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserModel>>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        PaginationMeta meta = new PaginationMeta(
                users.size(),
                users.size(),
                1,
                1,
                false,
                false
        );

        ApiResponse<List<UserModel>> response = new ApiResponse<>(
                true,
                users,
                null,
                "Users fetched successfully",
                meta
        );
        return ResponseEntity.ok(response);
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Long userId) {
        Optional<UserModel> user = userService.getUserById(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    user.get(),
                    null,
                    "User found",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }

    //Update user by ID
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserModel updatedUser) {

        Optional<UserModel> user = userService.updateUser(userId, updatedUser);

        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    user.get(),
                    null,
                    "User updated successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }


    //Get user's push token
    @GetMapping("/{userId}/push-tokens")
    public ResponseEntity<ApiResponse<?>> getPushToken(@PathVariable Long userId) {
        try {
            // First check if user exists
            Optional<UserModel> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(
                        false,
                        null,
                        "User not found",
                        "No user exists with that ID",
                        null
                ));
            }

            // Then get push token
            Optional<String> pushToken = userService.getPushToken(userId);

            if (pushToken.isPresent() && pushToken.get() != null && !pushToken.get().isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>(
                        true,
                        Map.of("push_token", pushToken.get()),
                        null,
                        "Push token fetched successfully",
                        null
                ));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(
                        false,
                        null,
                        "Push token not found",
                        "No push token associated with this user",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "Push token not found",
                    "No push token associated with this user",
                    null
            ));
        }
    }



    // Add or update user's push token
    @PostMapping("/{userId}/push-tokens")
    public ResponseEntity<ApiResponse<?>> addOrUpdatePushToken(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {

        String pushToken = requestBody.get("push_token");

        if (pushToken == null || pushToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    null,
                    "Invalid input",
                    "Push token is required",
                    null
            ));
        }

        Optional<UserModel> updatedUser = userService.updatePushToken(userId, pushToken);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    Map.of("push_token", updatedUser.get().getPushToken()),
                    null,
                    "Push token updated successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }




    // Delete user's push token
    @DeleteMapping("/{userId}/push-tokens")
    public ResponseEntity<ApiResponse<?>> deletePushToken(@PathVariable Long userId) {
        Optional<UserModel> user = userService.deletePushToken(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    null,
                    null,
                    "Push token deleted successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }


    //To view user preference with userid
    @GetMapping("/{userId}/preferences")
    public ResponseEntity<ApiResponse<?>> getPreferences(@PathVariable Long userId) {
        Optional<Map<String, Boolean>> preferences = userService.getPreferences(userId);

        if (preferences.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    preferences.get(),
                    null,
                    "Preferences fetched successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }


    //Update user preferences
    @PatchMapping("/{userId}/preferences")
    public ResponseEntity<ApiResponse<?>> updatePreferences(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> preferencesRequest) {

        Boolean emailEnabled = preferencesRequest.get("email_enabled");
        Boolean pushEnabled = preferencesRequest.get("push_enabled");

        if (emailEnabled == null || pushEnabled == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    null,
                    "Invalid input",
                    "Both email_enabled and push_enabled must be provided",
                    null
            ));
        }

        Optional<UserModel> updatedUser = userService.updatePreferences(userId, emailEnabled, pushEnabled);

        if (updatedUser.isPresent()) {
            Map<String, Boolean> data = Map.of(
                    "email_enabled", updatedUser.get().isEmailEnabled(),
                    "push_enabled", updatedUser.get().isPushEnabled()
            );

            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    data,
                    null,
                    "Preferences updated successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(
                    false,
                    null,
                    "User not found",
                    "No user exists with that ID",
                    null
            ));
        }
    }



}