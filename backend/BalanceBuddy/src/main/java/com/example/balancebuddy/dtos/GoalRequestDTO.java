package com.example.balancebuddy.dtos;

import com.example.balancebuddy.enums.Periodicity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GoalRequestDTO {

    @Getter
    @Setter
    private int userID;

    @Getter
    @Setter
    private Periodicity periodicity;

    @Getter
    @Setter
    private String target;

    @Getter
    @Setter
    private String habits;
}
