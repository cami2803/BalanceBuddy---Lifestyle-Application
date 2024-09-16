package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.ChangePasswordDTO;
import com.example.balancebuddy.dtos.NotificationSettingsDTO;
import com.example.balancebuddy.dtos.UpdateUserDTO;
import com.example.balancebuddy.dtos.UserDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.services.GoalService;
import com.example.balancebuddy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private LogoutHandler logoutHandler;

    @Mock
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUser_Success() {
        MyUser mockUser = new MyUser("test@example.com", "password");
        mockUser.setFirstname("John");
        mockUser.setLastname("Doe");

        UserDTO expectedUserDTO = UserDTO.from(mockUser);

        ResponseEntity<UserDTO> responseEntity = userController.getCurrentUser(mockUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUserDTO, responseEntity.getBody());
    }

    @Test
    void testGetCurrentUser_Unauthenticated() {
        assertThrows(ResponseStatusException.class, () -> userController.getCurrentUser(null));
    }

    @Test
    void testUpdateUser_Success() {
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("new@example.com")
                .build();

        ResponseEntity<?> responseEntity = userController.updateUser(1, updateUserDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).myUpdateUser(1, updateUserDTO);
    }

    @Test
    void testUpdateUser_NotFound() {
        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("new@example.com")
                .build();

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
                .when(userService).myUpdateUser(eq(1), eq(updateUserDTO));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.updateUser(1, updateUserDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void testChangePassword_Success() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setEmail("test@example.com");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("NewPassw0rd!");

        MyUser mockUser = new MyUser();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedOldPassword");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        doNothing().when(userService).myChangePassword(anyString(), anyString(), anyString());
        doNothing().when(logoutHandler).logout(any(), any(), any());

        ResponseEntity<?> responseEntity = userController.changePassword(changePasswordDTO, mockUser, null, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).myChangePassword(eq("test@example.com"), eq("oldPassword"), eq("NewPassw0rd!"));
        verify(logoutHandler, times(1)).logout(any(), any(), any());
    }

    @Test
    void testGetGoalsByUserId_NoContent() {
        when(goalService.findGoalsByUserId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Goal>> responseEntity = userController.getGoalsByUserId(1);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserDetailsById_NotFound() {
        when(userService.findByID(1)).thenReturn(Optional.empty());

        ResponseEntity<MyUser> responseEntity = userController.getUserDetailsById(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteUser_NotFound() {
        doThrow(new UsernameNotFoundException("User not found"))
                .when(userService).deleteUser(eq("nonexistent@example.com"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userController.deleteUser("nonexistent@example.com")
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateNotificationSettings_NotFound() {
        NotificationSettingsDTO settingsDTO = new NotificationSettingsDTO();

        doThrow(new UsernameNotFoundException("User not found"))
                .when(userService).updateNotificationSettings(eq(1), eq(settingsDTO));

        ResponseEntity<?> responseEntity = userController.updateNotificationSettings(1, settingsDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    void testGetNotificationSettings_NotFound() {
        when(userService.getNotificationSettings(1)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<NotificationSettingsDTO> responseEntity = userController.getNotificationSettings(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void testCreateUser_Exception() {
        MyUser user = new MyUser("test@example.com", "password");

        doThrow(new RuntimeException("Unexpected error"))
                .when(userService).createUser(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userController.createUser(user));

        assertEquals("Unexpected error", exception.getMessage());
    }

}
