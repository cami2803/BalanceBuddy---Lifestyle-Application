package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.entities.ProgressData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProgressServiceTest {

    @InjectMocks
    private ProgressService progressService;

    @Mock
    private HabitService habitService;

    @Mock
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProgressForUser() {
        Integer userID = 1;

        Habit habit1 = new Habit();
        habit1.setName("habit1");
        habit1.setUnit("units");

        Habit habit2 = new Habit();
        habit2.setName("habit2");
        habit2.setUnit("kg");

        when(habitService.findHabitByName("habit1")).thenReturn(habit1);
        when(habitService.findHabitByName("habit2")).thenReturn(habit2);

        Goal goal = new Goal();
        goal.setHabits("habit1;habit2");
        goal.setTarget("10;20");
        goal.setProgress("5;15");

        when(goalService.findGoalsByUserId(userID)).thenReturn(List.of(goal));

        ProgressData progressData = progressService.getProgressForUser(userID);

        Map<String, Integer> expectedProgress = Map.of("habit1", 5, "habit2", 15);
        Map<String, Integer> expectedTarget = Map.of("habit1", 10, "habit2", 20);
        Map<String, String> expectedUnit = Map.of("habit1", "units", "habit2", "kg");

        assertEquals(expectedProgress, progressData.getProgress());
        assertEquals(expectedTarget, progressData.getTarget());
        assertEquals(expectedUnit, progressData.getUnit());
    }
}
