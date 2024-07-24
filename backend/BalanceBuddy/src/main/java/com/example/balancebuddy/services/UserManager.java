package com.example.balancebuddy.services;

import java.text.MessageFormat;
import java.util.Optional;

import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserManager implements UserDetailsManager {

    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        MyUser myUser = (MyUser) user;
        myUser.setPassword(passwordEncoder.encode(user.getPassword()));
        myUser.setRole(Role.USER);
        userService.createUser(myUser);
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return userService.findByEmail(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<MyUser> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} not found", email));
        }
        return userOptional.get();
    }
}
