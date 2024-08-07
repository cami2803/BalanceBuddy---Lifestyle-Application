package com.example.balancebuddy.exceptions;

public class UserNotFoundException extends CustomExceptions{

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    public UserNotFoundException(int userID) {
        super("User not found with ID: " + userID);
    }
}
