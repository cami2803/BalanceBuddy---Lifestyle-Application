package com.example.balancebuddy.config;

import com.example.balancebuddy.entities.MyUser;
import org.springframework.stereotype.Component;

import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

// Converts a JWT into a UsernamePasswordAuthenticationToken
@Component
public class JWTToUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    // Method extracts the user ID from the JWT's subject field and creates a MyUser object
    // This token is then used by Spring Security to authenticate the user in the application
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        MyUser user = new MyUser();
        user.setUserID(Integer.parseInt(source.getSubject()));
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }
}
