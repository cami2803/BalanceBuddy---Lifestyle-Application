package com.example.balancebuddy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationUtils {

    private static final Logger logger = LoggerFactory.getLogger(NotificationUtils.class);

    public void sendNotification(String recipient, String message) {
        logger.info("Sending notification to {}: {}", recipient, message);
    }
}
