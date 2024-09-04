package com.example.balancebuddy.config;

import com.example.balancebuddy.entities.Token;
import com.example.balancebuddy.entities.MyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtEncoder accessTokenEncoder;

    @Qualifier("jwtRefreshTokenEncoder")
    private final JwtEncoder refreshTokenEncoder;

    // Creates an access token with a 120 minutes (2 hours) expiration time
    private String createAccessToken(Authentication authentication) {
        MyUser user = (MyUser) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(120, ChronoUnit.MINUTES))
                .subject(user.convertUserIDToString(user.getUserID()))
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    // Creates a refresh token with a 30 day expiration time
    private String createRefreshToken(Authentication authentication) {
        MyUser user = (MyUser) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(user.convertUserIDToString(user.getUserID()))
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    // Creates and returns a token object containing both access and refresh tokens
    public Token createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof MyUser user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass())
            );
        }

        Token tokenDTO = new Token();
        tokenDTO.setUserID(user.getUserID());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            // Generate new refresh token if it expires in less than 7 days
            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }

}
