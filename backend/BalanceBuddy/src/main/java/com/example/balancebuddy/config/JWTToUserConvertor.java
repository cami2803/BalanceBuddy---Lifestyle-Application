package com.example.balancebuddy.config;

import com.example.balancebuddy.entities.MyUser;
import org.springframework.stereotype.Component;

import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

// Converts a JWT (JSON Web Token) into a UsernamePasswordAuthenticationToken which is designed for simple
// presentation of a username and a password
@Component
public class JWTToUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    // This method extracts the user ID from the JWT's subject field, creates a MyUser object and encapsulates
    // it in a UsernamePasswordAuthenticationToken with no authorities. This token is then used by Spring Security
    // to authenticate the user in the application
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        MyUser user = new MyUser();
        user.setUserID(Integer.parseInt(source.getSubject()));
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }
}
