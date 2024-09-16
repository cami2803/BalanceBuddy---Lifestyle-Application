package com.example.balancebuddy.controllers;

import com.example.balancebuddy.services.ScheduledTasksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private ScheduledTasksService scheduledTasksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTriggerNotifications() {
        ResponseEntity<String> response = notificationController.triggerNotifications();

        verify(scheduledTasksService).sendPeriodicNotifications();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Periodic notifications triggered", response.getBody());
    }

    @Test
    void testTriggerReports() {
        ResponseEntity<String> response = notificationController.triggerReports();

        verify(scheduledTasksService, timeout(100)).generateDailyReports();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Daily reports triggered", response.getBody());
    }
}
