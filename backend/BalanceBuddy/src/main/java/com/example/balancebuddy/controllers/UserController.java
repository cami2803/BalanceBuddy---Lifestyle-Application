package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.UserDTO;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    // Endpoint to list all users in the database
    @GetMapping(value = "/list")
    public ResponseEntity<List<MyUser>> getAllUsers(){
        List<MyUser> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint for creating a new user
    @PostMapping(value = "/insert")
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser user){
        MyUser newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Endpoint to test the retrieval of the ID of a user
    // id appears in the request url
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable Integer id) {
        return ResponseEntity.ok(UserDTO.from(userService.findByID(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))));
    }

    // More secure endpoint to retrieve the id of the current connected user
    // id does not appear in the request url
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal MyUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(UserDTO.from(authenticatedUser));
    }
}
