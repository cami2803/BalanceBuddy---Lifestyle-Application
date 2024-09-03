package com.example.balancebuddy.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserDTO {

    private String firstname;

    private String lastname;

    private String email;
}
