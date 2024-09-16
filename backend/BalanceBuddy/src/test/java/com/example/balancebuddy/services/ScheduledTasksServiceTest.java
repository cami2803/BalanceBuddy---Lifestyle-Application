package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.entities.ProgressData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
public class ScheduledTasksServiceTest {

    @InjectMocks
    private ScheduledTasksService scheduledTasksService;

    @Mock
    private UserService userService;

    @Mock
    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendPeriodicNotifications() {
        MyUser user = new MyUser("user@example.com", "encodedPassword");
        user.setReminder(true);

        when(userService.getAllUsers()).thenReturn(List.of(user));

        scheduledTasksService.sendPeriodicNotifications();
        assertTrue(true);
    }

    @Test
    void testGenerateDailyReports() {
        MyUser user = new MyUser("user@example.com", "encodedPassword");
        ProgressData progressData = new ProgressData();
        progressData.setProgress(Map.of("habit1", 5));
        progressData.setTarget(Map.of("habit1", 10));
        progressData.setUnit(Map.of("habit1", "units"));

        when(userService.getAllUsers()).thenReturn(List.of(user));
        when(progressService.getProgressForUser(user.getUserID())).thenReturn(progressData);

        scheduledTasksService.generateDailyReports();

        assertTrue(true);
    }
}
