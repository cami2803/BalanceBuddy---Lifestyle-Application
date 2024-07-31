package com.example.balancebuddy.dtos;

import com.example.balancebuddy.entities.Periodicity;
import lombok.Data;

@Data
public class GoalRequestDTO {

    private int userID;

    private Periodicity periodicity;

    private String target;

    private String habits;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }
}
