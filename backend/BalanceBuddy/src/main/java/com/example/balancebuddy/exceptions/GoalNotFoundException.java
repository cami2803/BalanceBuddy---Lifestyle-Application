package com.example.balancebuddy.exceptions;

public class GoalNotFoundException extends CustomExceptions{

    private static final long serialVersionUID = 1L;

    public GoalNotFoundException(int goalID) {
        super("Goal not found with ID: " + goalID);
    }
}
