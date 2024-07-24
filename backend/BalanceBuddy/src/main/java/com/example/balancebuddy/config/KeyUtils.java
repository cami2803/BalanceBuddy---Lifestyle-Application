package com.example.balancebuddy.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Component
@Slf4j
public class KeyUtils {
    @Autowired
    Environment environment;

    // Path to the private key for access tokens
    @Value("${ACCESS_TOKEN_PRIVATE_KEY_PATH}")
    private String accessTokenPrivateKeyPath;

    // Path to the public key for access tokens
    @Value("${ACCESS_TOKEN_PUBLIC_KEY_PATH}")
    private String accessTokenPublicKeyPath;

    // Path to the private key for refresh tokens
    @Value("${REFRESH_TOKEN_PRIVATE_KEY_PATH}")
    private String refreshTokenPrivateKeyPath;

    // Path to the public key for refresh tokens
    @Value("${REFRESH_TOKEN_PUBLIC_KEY_PATH}")
    private String refreshTokenPublicKeyPath;

    private KeyPair _accessTokenKeyPair;
    private KeyPair _refreshTokenKeyPair;

    // Retrieves key pair for access tokens, generating it if necessary
    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)) {
            _accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return _accessTokenKeyPair;
    }

    // Retrieves the key pair for refresh tokens, generating it if necessary
    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(_refreshTokenKeyPair)) {
            _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return _refreshTokenKeyPair;
    }

    // Retrieves the key pair from the given public and private key paths.
    // If the keys do not exist, they will be generated and saved to the specified paths.
    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;

        // Create File objects for the public and private key paths
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        // Check if both files exist
        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            log.info("loading keys from file: {}, {}", publicKeyPath, privateKeyPath);
            try {
                // Initialize a KeyFactory for the RSA algorithm (RSA user creates and publishes a public key based
                // on two large prime numbers (that are kept secret), and an auxiliary value. Messages
                // can be encrypted by anyone, via the public key, but can only be decrypted by someone who knows the private key)
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                // Read the public key bytes from the file and generate a PublicKey object
                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                // Read the private key bytes from the file and generate a PublicKey object
                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                // Create a KeyPair object from the public and private keys
                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            // If in production and keys don't exist, throw an exception
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                throw new RuntimeException("public and private keys don't exist");
            }
        }

        // Create a directory to store the generated keys if it doesn't exist
        File directory = new File("access-refresh-token-keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);

            // Generate a new RSA key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // 2048-bit key size for security
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

            // Save the generated public key to the specified file
            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            // Save the generated private key to the specified file
            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        // Return the generated key pair
        return keyPair;
    }

    // Retrieves public key for access tokens
    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    // Retrieves private key for access tokens
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    // Retrieves public key for refresh tokens
    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    // Retrieves private key for refresh tokens
    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }
}
