package com.example.balancebuddy.services;

import com.example.balancebuddy.observers.Observer;
import org.springframework.stereotype.Service;

@Service
public class ReportGeneratorService implements Observer {

    @Override
    public void update(String habitData) {
        System.out.println("ReportGenerator: Generating report with habit data: " + habitData);
    }
}
