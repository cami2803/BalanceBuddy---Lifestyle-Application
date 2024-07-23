package com.example.balancebuddy.dtos;

import com.example.balancebuddy.entities.MyUser;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private String id;
    private String username;

    public static UserDTO from(MyUser user) {
        return UserDTO.builder()  // Corrected the reference to the builder
                .id(user.convertUserIDToString(user.getUserID()))
                .username(user.getUsername())
                .build();
    }
}
