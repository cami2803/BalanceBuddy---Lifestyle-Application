package com.example.balancebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BalanceBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BalanceBuddyApplication.class, args);
    }

}
