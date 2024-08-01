package com.example.balancebuddy.entities;

import lombok.Getter;
import lombok.Setter;

public class Token {

    @Getter
    @Setter
    private int userID;

    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String refreshToken;
}
