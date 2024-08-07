package com.example.balancebuddy.exceptions;

public class HabitNotFoundException extends CustomExceptions{

    private static final long serialVersionUID = 1L;

    public HabitNotFoundException(String habitName) {
        super("Habit not found with name: " + habitName);
    }

    public HabitNotFoundException(int habitID) {
        super("Habit not found with ID: " + habitID);
    }
}
