package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.AddHabitToGoalDTO;
import com.example.balancebuddy.dtos.GoalRequestDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    GoalService goalService;

    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals(){
        List<Goal> goals = goalService.getAllGoals();
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalRequestDTO goalRequestDTO){
        Goal createdGoal = goalService.createGoal(goalRequestDTO);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalByID(@PathVariable int id){
        Goal goal = goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable int id, @RequestBody Goal goal){
        goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        Goal updatedGoal = goalService.updateGoal(id, goal);
        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable int id){
        goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        goalService.deleteGoal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/addHabit")
    public ResponseEntity<Goal> addHabitToGoal(@PathVariable int id, @RequestBody AddHabitToGoalDTO habit){
        goalService.findGoalByID(id).orElseThrow(() -> new RuntimeException("Goal not found!"));
        Goal newGoal = goalService.addHabitToGoal(id, habit.getName(), habit.getTarget());
        return new ResponseEntity<>(newGoal, HttpStatus.OK);
    }
}
