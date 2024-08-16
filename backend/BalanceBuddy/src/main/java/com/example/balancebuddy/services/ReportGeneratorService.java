package com.example.balancebuddy.services;

import com.example.balancebuddy.observers.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReportGeneratorService implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorService.class);

    @Override
    public void update(String habitData) {
        logger.info("ReportGenerator: Generating report with habit data: {}", habitData);
    }
}
