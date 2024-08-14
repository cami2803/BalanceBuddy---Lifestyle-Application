package com.example.balancebuddy.controllers;

import com.example.balancebuddy.config.TokenGenerator;
import com.example.balancebuddy.entities.*;
import com.example.balancebuddy.services.UserService;
import com.example.balancebuddy.utils.ValidationUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final TokenGenerator tokenGenerator;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Qualifier("jwtRefreshTokenAuthProvider")
    private final JwtAuthenticationProvider refreshTokenAuthProvider;

    private final LogoutHandler logoutHandler;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignUp signupDTO) {
        List<String> errors = ValidationUtils.validateSignUp(signupDTO);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        MyUser user = new MyUser(signupDTO.getEmail(), signupDTO.getPassword());
        user.setFirstname(signupDTO.getFirstname());
        user.setLastname(signupDTO.getLastname());

        userService.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Collections.EMPTY_LIST);

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Login loginDTO) {
        List<String> errors = ValidationUtils.validateLogin(loginDTO);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Authenticate user
            Authentication authentication = daoAuthenticationProvider.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getEmail(), loginDTO.getPassword())
            );
            // Generate tokens
            Token token = tokenGenerator.createToken(authentication);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Endpoint for creating a new access token from the refresh token
    @PostMapping("/token")
    public ResponseEntity getNewAccessToken(@RequestBody Token tokenDTO) {
        try {
            System.out.println("Received refresh token: " + tokenDTO.getRefreshToken());
            Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));

            Token newTokens = tokenGenerator.createToken(authentication);

            System.out.println("Generated new tokens");
            return ResponseEntity.ok(newTokens);
        } catch (ExpiredJwtException e) {
            System.err.println("Refresh token expired");
            return ResponseEntity.status(401).body("Refresh token expired. Please log in again.");
        } catch (JwtException e) {
            System.err.println("Invalid refresh token");
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }

    // Endpoint for user-initiated logout
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        logoutHandler.logout(request, response, null);
        //return ResponseEntity.ok("User logged out successfully.");
        return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully."));
    }

    // Endpoint to test user access only if authenticated
    @GetMapping("/secure-endpoint")
    public ResponseEntity<?> secureEndpoint(HttpServletRequest request) {
        try {
            return ResponseEntity.ok("Secure data");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Access token expired or invalid");
        }
    }

}
