package com.example.balancebuddy.services;

import com.example.balancebuddy.observers.Observer;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Observer {

    @Override
    public void update(String habitData) {
        System.out.println("NotificationService: New habit data: " + habitData);
    }
}
