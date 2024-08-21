package com.example.balancebuddy.dtos;

import com.example.balancebuddy.enums.Periodicity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalRequestDTO {

    private int userID;

    private Periodicity periodicity;

    private String target;

    private String habits;
}
