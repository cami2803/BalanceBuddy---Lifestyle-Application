package com.example.balancebuddy.controllers;

import com.example.balancebuddy.services.ScheduledTasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class NotificationController {

    private final ScheduledTasksService scheduledTasksService;

    // Endpoint to trigger periodic notifications manually
    @PostMapping("/trigger-notifications")
    public ResponseEntity<String> triggerNotifications() {
        //scheduledTasksService.sendPeriodicNotifications();
        CompletableFuture.runAsync(scheduledTasksService::sendPeriodicNotifications);
        return new ResponseEntity<>("Periodic notifications triggered", HttpStatus.OK);
    }

    // Endpoint to trigger daily reports manually
    @PostMapping("/trigger-reports")
    public ResponseEntity<String> triggerReports() {
        //scheduledTasksService.generateDailyReports();
        CompletableFuture.runAsync(scheduledTasksService::generateDailyReports);
        return new ResponseEntity<>("Daily reports triggered", HttpStatus.OK);
    }
}
