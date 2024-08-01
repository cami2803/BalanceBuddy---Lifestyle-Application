package com.example.balancebuddy.entities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class Login {
    @NotNull
    @Getter
    @Setter
    private String email;

    @NotNull
    @Getter
    @Setter
    private String password;
}
