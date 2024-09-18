package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.AddHabitToGoalDTO;
import com.example.balancebuddy.dtos.GoalRequestDTO;
import com.example.balancebuddy.dtos.ProgressUpdateDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.enums.Periodicity;
import com.example.balancebuddy.exceptions.GoalNotFoundException;
import com.example.balancebuddy.services.GoalService;
import com.example.balancebuddy.subjects.UserActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GoalControllerTest {

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalController goalController;

    @Mock
    private UserActivity userActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGoals() {
        Goal goal = new Goal();
        when(goalService.getAllGoals()).thenReturn(Collections.singletonList(goal));

        ResponseEntity<List<Goal>> response = goalController.getAllGoals();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testCreateGoal() {
        GoalRequestDTO goalRequestDTO = new GoalRequestDTO();
        goalRequestDTO.setUserID(1);
        goalRequestDTO.setPeriodicity(Periodicity.DAILY);
        goalRequestDTO.setTarget("10");
        goalRequestDTO.setHabits("exercise");

        Goal createdGoal = new Goal();
        when(goalService.createGoal(any(GoalRequestDTO.class))).thenReturn(createdGoal);

        ResponseEntity<Goal> response = goalController.createGoal(goalRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdGoal, response.getBody());
    }

    @Test
    void testGetGoalByID() {
        Goal goal = new Goal();
        when(goalService.findGoalByID(1)).thenReturn(Optional.of(goal));

        ResponseEntity<Goal> response = goalController.getGoalByID(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goal, response.getBody());
    }

    @Test
    void testUpdateGoal() {
        Goal goal = new Goal();
        when(goalService.updateGoal(eq(1), any(Goal.class))).thenReturn(goal);

        ResponseEntity<Goal> response = (ResponseEntity<Goal>) goalController.updateGoal(1, goal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goal, response.getBody());
    }

    @Test
    void testUpdateGoal_NotFound() {
        Goal goal = new Goal();
        when(goalService.updateGoal(eq(1), any(Goal.class))).thenThrow(new GoalNotFoundException(1));

        ResponseEntity<String> response = (ResponseEntity<String>) goalController.updateGoal(1, goal);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Goal not found: Goal not found with ID: 1", response.getBody());
    }


    @Test
    void testDeleteGoal() {
        doNothing().when(goalService).deleteGoal(1);
        when(goalService.findGoalByID(1)).thenReturn(Optional.of(new Goal()));

        ResponseEntity<Void> response = goalController.deleteGoal(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAddHabitToGoal() {
        AddHabitToGoalDTO addHabitToGoalDTO = new AddHabitToGoalDTO();
        addHabitToGoalDTO.setName("exercise");
        addHabitToGoalDTO.setTarget("30min");

        Goal existingGoal = new Goal();
        existingGoal.setGoalID(1);
        existingGoal.setHabits("running");
        existingGoal.setTarget("10min");
        existingGoal.setProgress("0");

        Goal updatedGoal = new Goal();
        updatedGoal.setGoalID(1);
        updatedGoal.setHabits("running;exercise");
        updatedGoal.setTarget("10min;30min");
        updatedGoal.setProgress("0;0");

        when(goalService.findGoalByID(1)).thenReturn(Optional.of(existingGoal));
        when(goalService.addHabitToGoal(eq(1), anyString(), anyString())).thenReturn(updatedGoal);

        ResponseEntity<Goal> response = goalController.addHabitToGoal(1, addHabitToGoalDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGoal, response.getBody());
    }


    @Test
    void testUpdateProgress() {
        ProgressUpdateDTO progressUpdateDTO = new ProgressUpdateDTO();
        progressUpdateDTO.setHabitName("exercise");
        progressUpdateDTO.setProgressValue(20);

        ResponseEntity<Void> response = goalController.updateProgress(1, progressUpdateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
