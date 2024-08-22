package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.AddHabitToGoalDTO;
import com.example.balancebuddy.dtos.GoalRequestDTO;
import com.example.balancebuddy.dtos.ProgressUpdateDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.exceptions.GoalNotFoundException;
import com.example.balancebuddy.services.GoalService;
import com.example.balancebuddy.services.NotificationService;
import com.example.balancebuddy.services.ReportGeneratorService;
import com.example.balancebuddy.subjects.UserActivity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    private final NotificationService notificationService;

    private final ReportGeneratorService reportGenerator;

    private final UserActivity userActivity;

    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @PostConstruct
    private void init() {
        userActivity.addObserver(notificationService);
        userActivity.addObserver(reportGenerator);
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals() {
        List<Goal> goals = goalService.getAllGoals();
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalRequestDTO goalRequestDTO) {
        Goal createdGoal = goalService.createGoal(goalRequestDTO);
        userActivity.setHabitData(goalRequestDTO.getHabits());
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalByID(@PathVariable int id) {
        Goal goal = goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable int id, @RequestBody Goal goal) {
        try {
            logger.info("Updating goal with ID: {}", id);
            logger.info("Received goal data: {}", goal);

            Goal updatedGoal = goalService.updateGoal(id, goal);
            return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
        } catch (GoalNotFoundException e) {
            logger.error("Goal not found: {}", e.getMessage());
            return new ResponseEntity<>("Goal not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating goal: {}", e.getMessage());
            return new ResponseEntity<>("Error updating goal: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable int id) {
        goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        goalService.deleteGoal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/addHabit")
    public ResponseEntity<Goal> addHabitToGoal(@PathVariable int id, @RequestBody AddHabitToGoalDTO habit) {
        goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        Goal newGoal = goalService.addHabitToGoal(id, habit.getName(), habit.getTarget());

        userActivity.setHabitData(habit.getName() + ";" + habit.getTarget());

        return new ResponseEntity<>(newGoal, HttpStatus.OK);
    }

    @PostMapping("/{id}/updateProgress")
    public ResponseEntity<Void> updateProgress(@PathVariable int id, @RequestBody ProgressUpdateDTO progressUpdateDTO) {
        goalService.updateProgress(id, progressUpdateDTO.getHabitName(), progressUpdateDTO.getProgressValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
