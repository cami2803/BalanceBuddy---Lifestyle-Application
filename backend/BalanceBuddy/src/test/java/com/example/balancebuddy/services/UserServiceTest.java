package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.enums.Role;
import com.example.balancebuddy.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TypedQuery<MyUser> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByID_UserExists() {
        MyUser mockUser = new MyUser(2, "Test", "Test", "test@example.com", "encodedOldPassword", Role.USER, true, true);
        when(entityManager.find(MyUser.class, 2)).thenReturn(mockUser);

        Optional<MyUser> result = userService.findByID(2);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testFindByID_UserNotFound() {
        when(entityManager.find(MyUser.class, 1)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findByID(1));
    }


    @Test
    void testMyChangePassword_InvalidOldPassword() {
        MyUser user = new MyUser(2, "Test", "Test", "test@example.com", "encodedOldPassword", Role.USER, true, true);
        TypedQuery<MyUser> query = mock(TypedQuery.class);

        when(query.getSingleResult()).thenReturn(user);
        when(entityManager.createQuery("SELECT u FROM MyUser u WHERE u.email = :email", MyUser.class)).thenReturn(query);
        when(query.setParameter("email", "test@example.com")).thenReturn(query);
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.myChangePassword("test@example.com", "oldPassword", "newPassword"));
    }


    @Test
    void testMyChangePassword_UserNotFound() {
        TypedQuery<MyUser> query = mock(TypedQuery.class);

        when(query.getSingleResult()).thenThrow(NoResultException.class);
        when(entityManager.createQuery("SELECT u FROM MyUser u WHERE u.email = :email", MyUser.class)).thenReturn(query);
        when(query.setParameter("email", "test@example.com")).thenReturn(query);

        assertThrows(UserNotFoundException.class, () -> userService.myChangePassword("test@example.com", "oldPassword", "newPassword"));
    }


}
