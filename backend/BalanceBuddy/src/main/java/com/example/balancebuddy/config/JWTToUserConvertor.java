package com.example.balancebuddy.config;

import com.example.balancebuddy.entities.MyUser;
import org.springframework.stereotype.Component;

import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
public class JWTToUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        MyUser user = new MyUser();
        user.setUserID(Integer.parseInt(source.getSubject()));
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }
}
