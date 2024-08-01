package com.example.balancebuddy.dtos;

import lombok.Getter;
import lombok.Setter;

public class ChangePasswordDTO {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String oldPassword;

    @Getter
    @Setter
    private String newPassword;
}
