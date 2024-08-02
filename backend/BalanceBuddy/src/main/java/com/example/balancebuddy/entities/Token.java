package com.example.balancebuddy.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {


    private int userID;

    private String accessToken;

    private String refreshToken;
}
