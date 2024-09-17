package com.example.balancebuddy.controllers;

import com.example.balancebuddy.config.TokenGenerator;
import com.example.balancebuddy.entities.Login;
import com.example.balancebuddy.entities.SignUp;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.entities.Token;
import com.example.balancebuddy.services.UserService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Mock
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    @Mock
    private LogoutHandler logoutHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        SignUp signupDTO = new SignUp("test@example.com", "John", "Doe", "Password123!");

        MyUser user = new MyUser();
        user.setEmail(signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword());

        doNothing().when(userService).createUser(any(MyUser.class));

        Authentication authentication = mock(Authentication.class);
        Token mockToken = new Token();
        when(tokenGenerator.createToken(any(Authentication.class))).thenReturn(mockToken);

        ResponseEntity<Token> responseEntity = authController.register(signupDTO);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testRegister_Fail() {
        SignUp signupDTO = new SignUp("test@example.com", "John", "Doe", "wrong");


        ResponseEntity<List<String>> responseEntity = authController.register(signupDTO);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals(Collections.singletonList("Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character"), responseEntity.getBody());
    }


    @Test
    void testLogin_Success() {
        Login loginDTO = new Login();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("Password123!");

        MyUser user = new MyUser();
        user.setEmail(loginDTO.getEmail());
        user.setPassword(loginDTO.getPassword());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, loginDTO.getPassword(), Collections.emptyList());

        when(daoAuthenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Token mockToken = new Token();
        when(tokenGenerator.createToken(authentication)).thenReturn(mockToken);

        ResponseEntity<Token> responseEntity = authController.login(loginDTO);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testLogin_InvalidCredentials() {
        Login loginDTO = new Login();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongPassword");

        when(daoAuthenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<Token> responseEntity = authController.login(loginDTO);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void testToken_Success() {
        Token tokenDTO = new Token();
        tokenDTO.setRefreshToken("validRefreshToken");

        Authentication authentication = mock(Authentication.class);
        Token newToken = new Token();

        when(refreshTokenAuthProvider.authenticate(any(BearerTokenAuthenticationToken.class))).thenReturn(authentication);
        when(tokenGenerator.createToken(authentication)).thenReturn(newToken);

        ResponseEntity<Token> responseEntity = authController.getNewAccessToken(tokenDTO);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testToken_Fail() {
        Token tokenDTO = new Token();
        tokenDTO.setRefreshToken("invalidRefreshToken");

        when(refreshTokenAuthProvider.authenticate(any(BearerTokenAuthenticationToken.class)))
                .thenThrow(new JwtException("Invalid refresh token"));

        JwtException thrownException = assertThrows(JwtException.class, () ->
                authController.getNewAccessToken(tokenDTO)
        );

        assertEquals("Invalid refresh token", thrownException.getMessage());
    }



    @Test
    void testLogout_Success() {
        ResponseEntity<Map<String, String>> responseEntity = authController.logout(null, null);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(Collections.singletonMap("message", "User logged out successfully."), responseEntity.getBody());
    }

    @Test
    void testLogout_Fail() {
        doThrow(new RuntimeException("Logout failed")).when(logoutHandler).logout(any(), any(), any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.logout(null, null);
        });

        assertEquals("Logout failed", exception.getMessage());
    }


    @Test
    void testSecureEndpoint_Success() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<String> responseEntity = (ResponseEntity<String>) authController.secureEndpoint(null);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Secure data", responseEntity.getBody());
    }

}
