package com.example.balancebuddy.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressUpdateDTO {

    private String habitName;
    private int progressValue;
}
