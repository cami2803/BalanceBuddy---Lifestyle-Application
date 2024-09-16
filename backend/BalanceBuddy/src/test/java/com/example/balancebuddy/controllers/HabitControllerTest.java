package com.example.balancebuddy.controllers;

import com.example.balancebuddy.dtos.HabitRequestDTO;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.services.HabitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitControllerTest {

    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitController habitController;

    @Test
    void testGetAllHabits() {
        Habit habit1 = new Habit();
        Habit habit2 = new Habit();
        List<Habit> habits = List.of(habit1, habit2);

        when(habitService.getAllHabits()).thenReturn(habits);

        ResponseEntity<List<Habit>> response = habitController.getAllHabits();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(habitService, times(1)).getAllHabits();
    }

    @Test
    void testCreateHabit() {
        HabitRequestDTO habitRequestDTO = new HabitRequestDTO();
        Habit habit = new Habit();

        when(habitService.createHabit(any(HabitRequestDTO.class))).thenReturn(habit);

        ResponseEntity<Habit> response = habitController.createHabit(habitRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(habit, response.getBody());
        verify(habitService, times(1)).createHabit(any(HabitRequestDTO.class));
    }

    @Test
    void testGetHabitByID() {
        Habit habit = new Habit();
        when(habitService.findHabitByID(anyInt())).thenReturn(Optional.of(habit));

        ResponseEntity<Habit> response = habitController.getHabitByID(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(habit, response.getBody());
        verify(habitService, times(1)).findHabitByID(anyInt());
    }

    @Test
    void testGetHabitByID_NotFound() {
        when(habitService.findHabitByID(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> habitController.getHabitByID(1));

        assertEquals("Habit not found!", exception.getMessage());
        verify(habitService, times(1)).findHabitByID(anyInt());
    }

    @Test
    void testUpdateHabit() {
        Habit habit = new Habit();
        Optional<Habit> optionalHabit = Optional.of(habit);

        when(habitService.findHabitByID(anyInt())).thenReturn(optionalHabit);
        when(habitService.updateHabit(any(Habit.class))).thenReturn(habit);

        ResponseEntity<Habit> response = habitController.updateHabit(1, habit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(habit, response.getBody());
        verify(habitService, times(1)).findHabitByID(anyInt());
        verify(habitService, times(1)).updateHabit(any(Habit.class));
    }


    @Test
    void testUpdateHabit_NotFound() {
        when(habitService.findHabitByID(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> habitController.updateHabit(1, new Habit()));

        assertEquals("Habit not found!", exception.getMessage());
        verify(habitService, times(1)).findHabitByID(anyInt());
    }

    @Test
    void testDeleteHabit() {
        when(habitService.findHabitByID(anyInt())).thenReturn(Optional.of(new Habit()));

        ResponseEntity<Void> response = habitController.deleteHabit(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(habitService, times(1)).deleteHabit(anyInt());
    }

    @Test
    void testDeleteHabit_NotFound() {
        when(habitService.findHabitByID(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> habitController.deleteHabit(1));

        assertEquals("Habit not found!", exception.getMessage());
        verify(habitService, times(1)).findHabitByID(anyInt());
    }
}

