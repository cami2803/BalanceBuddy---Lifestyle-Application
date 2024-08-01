package com.example.balancebuddy.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class SignUp {

    @NotNull
    @Getter
    @Setter
    private String email;

    @NotNull
    @Getter
    @Setter
    private String firstname;

    @NotNull
    @Getter
    @Setter
    private String lastname;

    @NotNull
    @Getter
    @Setter
    private String password;

    @Override
    public String toString() {
        return "SignUP form has email: " + email + " and password: " + password;
    }
}
