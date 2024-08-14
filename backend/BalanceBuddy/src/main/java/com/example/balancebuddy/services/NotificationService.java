package com.example.balancebuddy.services;

import com.example.balancebuddy.observers.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Override
    public void update(String habitData) {
        logger.info("NotificationService: New habit data: {}", habitData);
    }
}
