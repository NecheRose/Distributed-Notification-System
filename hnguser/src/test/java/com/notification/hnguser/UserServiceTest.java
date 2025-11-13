package com.notification.hnguser;

import com.notification.hnguser.model.UserModel;
import com.notification.hnguser.repository.UserRepository;
import com.notification.hnguser.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;      // <-- for when(), verify(), any()
import static org.assertj.core.api.Assertions.assertThat; // <-- for assertThat()
import java.util.List;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createUser_ShouldSaveAndReturnUser() {
        // Arrange
        UserModel user = new UserModel();
        user.setName("Ndifreke");
        user.setEmail("ndifreke@example.com");
        user.setPassword("password123");

        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        UserModel result = userService.createUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertThat(result.getName()).isEqualTo("Ndifreke");
    }


    @Test
    void getUserById_ShouldReturnUser_WhenFound() {
        // Arrange
        UserModel user = new UserModel();
        user.setUserId(1L);
        user.setName("Jane");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<UserModel> result = userService.getUserById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane");
        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        UserModel user1 = new UserModel();
        user1.setName("Alice");

        UserModel user2 = new UserModel();
        user2.setName("Bob");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserModel> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void updatePreferences_ShouldUpdateUserPreferences() {
        // Arrange
        UserModel user = new UserModel();
        user.setUserId(1L);
        user.setEmailEnabled(false);
        user.setPushEnabled(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        var result = userService.updatePreferences(1L, true, true);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().isEmailEnabled()).isTrue();
        assertThat(result.get().isPushEnabled()).isTrue();
        verify(userRepository, times(1)).save(any(UserModel.class));
    }


    @Test
    void updatePushToken_ShouldSaveNewToken() {
        // Arrange
        UserModel user = new UserModel();
        user.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        var result = userService.updatePushToken(1L, "new-token");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getPushToken()).isEqualTo("new-token");
        verify(userRepository).save(any(UserModel.class));
    }






}
