package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<MyUser> getAllUsers(){
        return userRepository.findAll();
    }

    public MyUser createUser(MyUser user){
        return userRepository.save(user);
    }

}
