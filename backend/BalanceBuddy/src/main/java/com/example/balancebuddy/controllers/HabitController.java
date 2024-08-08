package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.HabitRequestDTO;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @GetMapping
    public ResponseEntity<List<Habit>> getAllHabits() {
        List<Habit> habits = habitService.getAllHabits();
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Habit> createHabit(@RequestBody HabitRequestDTO habitRequestDTO) {
        Habit createdHabit = habitService.createHabit(habitRequestDTO);
        return new ResponseEntity<>(createdHabit, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habit> getHabitByID(@PathVariable int id) {
        Habit habit = habitService.findHabitByID(id).orElseThrow(() -> new RuntimeException("Habit not found!"));
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habit> updateHabit(@PathVariable int id, @RequestBody Habit habit) {
        habitService.findHabitByID(id).orElseThrow(() -> new RuntimeException("Habit not found!"));
        Habit updatedHabit = habitService.updateHabit(habit);
        return new ResponseEntity<>(updatedHabit, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable int id) {
        habitService.findHabitByID(id).orElseThrow(() -> new RuntimeException("Habit not found!"));
        habitService.deleteHabit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
