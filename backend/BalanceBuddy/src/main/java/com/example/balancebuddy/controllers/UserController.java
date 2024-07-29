package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.ChangePasswordDTO;
import com.example.balancebuddy.dtos.UserDTO;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.services.UserService;
import com.example.balancebuddy.utils.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    LogoutHandler logoutHandler;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to list all users in the database
    @GetMapping(value = "/list")
    public ResponseEntity<List<MyUser>> getAllUsers() {
        List<MyUser> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint for creating a new user
    @PostMapping(value = "/insert")
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser user) {
        MyUser newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Endpoint to test the retrieval of the ID of a user
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable Integer id) {
        return ResponseEntity.ok(UserDTO.from(userService.findByID(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))));
    }

    // More secure endpoint to retrieve the id of the current connected user
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal MyUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(UserDTO.from(authenticatedUser));
    }

    // Endpoint to update a user
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody MyUser user) {
        userService.updateUser(user);
        return ResponseEntity.ok("User updated successfully!");
    }

    // Endpoint to delete a user
    @DeleteMapping("delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // Endpoint to change the password of a user based on its email
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request, @AuthenticationPrincipal MyUser authenticatedUser,
                                            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        List<String> errors = ValidationUtils.validateChangePassword(request);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        try {
            userService.myChangePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
            // Logout the user after password is reset
            logoutHandler.logout(httpRequest, httpResponse, null);
            return ResponseEntity.ok("Password changed successfully!");
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}