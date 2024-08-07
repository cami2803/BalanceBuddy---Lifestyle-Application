package com.example.balancebuddy.utils;

import org.springframework.stereotype.Component;

@Component
public class NotificationUtils {

    public void sendNotification(String recipient, String message){
        System.out.println("Sending notification to " + recipient + ": " + message);
    }
}
