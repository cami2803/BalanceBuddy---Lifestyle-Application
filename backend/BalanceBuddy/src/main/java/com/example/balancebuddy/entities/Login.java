package com.example.balancebuddy.entities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Login {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
