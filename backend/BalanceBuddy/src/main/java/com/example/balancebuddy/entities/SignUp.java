package com.example.balancebuddy.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignUp {
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public SignUp() {

    }

    public SignUp(String email, String password, String firstname, String lastname) {
        super();
        this.email = email;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
