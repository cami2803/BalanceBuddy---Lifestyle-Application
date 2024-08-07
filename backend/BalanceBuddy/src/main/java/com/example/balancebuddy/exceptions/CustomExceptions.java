package com.example.balancebuddy.exceptions;

public class CustomExceptions extends RuntimeException {

    private static final long serialVerionUID = 1L;

    public CustomExceptions(String message){
        super(message);
    }

    public CustomExceptions(String message, Throwable cause){
        super(message, cause);
    }
}
