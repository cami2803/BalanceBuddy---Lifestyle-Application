package com.example.balancebuddy.services;

import com.example.balancebuddy.dtos.HabitRequestDTO;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.exceptions.HabitNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private HabitService habitService;

    @Test
    void testGetAllHabits() {
        Habit habit1 = new Habit();
        Habit habit2 = new Habit();
        List<Habit> habits = List.of(habit1, habit2);

        TypedQuery<Habit> typedQuery = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT h from Habit h", Habit.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(habits);

        List<Habit> result = habitService.getAllHabits();

        assertEquals(2, result.size());
        verify(entityManager, times(1)).createQuery("SELECT h from Habit h", Habit.class);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    void testCreateHabit() {
        HabitRequestDTO habitRequestDTO = new HabitRequestDTO();
        habitRequestDTO.setName("Test Habit");
        habitRequestDTO.setUnit("Times");

        Habit habit = new Habit();
        habit.setName("Test Habit");
        habit.setUnit("Times");

        doNothing().when(entityManager).persist(any(Habit.class));

        Habit result = habitService.createHabit(habitRequestDTO);

        assertEquals("Test Habit", result.getName());
        assertEquals("Times", result.getUnit());
        verify(entityManager, times(1)).persist(any(Habit.class));
    }

    @Test
    void testFindHabitByID() {
        Habit habit = new Habit();
        habit.setHabitID(1);

        when(entityManager.find(Habit.class, 1)).thenReturn(habit);

        Optional<Habit> result = habitService.findHabitByID(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getHabitID());
        verify(entityManager, times(1)).find(Habit.class, 1);
    }

    @Test
    void testFindHabitByID_NotFound() {
        when(entityManager.find(Habit.class, 1)).thenReturn(null);

        assertThrows(HabitNotFoundException.class, () -> habitService.findHabitByID(1));
        verify(entityManager, times(1)).find(Habit.class, 1);
    }

    @Test
    void testUpdateHabit() {
        Habit habit = new Habit();
        habit.setName("Updated Habit");

        when(entityManager.merge(any(Habit.class))).thenReturn(habit);

        Habit result = habitService.updateHabit(habit);

        assertEquals("Updated Habit", result.getName());
        verify(entityManager, times(1)).merge(habit);
    }

    @Test
    void testDeleteHabit() {
        Habit habit = new Habit();
        habit.setHabitID(1);

        when(entityManager.find(Habit.class, 1)).thenReturn(habit);
        doNothing().when(entityManager).remove(any(Habit.class));

        habitService.deleteHabit(1);

        verify(entityManager, times(1)).find(Habit.class, 1);
        verify(entityManager, times(1)).remove(habit);
    }

    @Test
    void testDeleteHabit_NotFound() {
        when(entityManager.find(Habit.class, 1)).thenReturn(null);

        assertThrows(HabitNotFoundException.class, () -> habitService.deleteHabit(1));
        verify(entityManager, times(1)).find(Habit.class, 1);
    }

    @Test
    void testFindHabitByName() {
        Habit habit = new Habit();
        habit.setName("Test Habit");

        TypedQuery<Habit> typedQuery = mock(TypedQuery.class);

        when(typedQuery.getResultStream()).thenReturn(List.of(habit).stream());
        when(entityManager.createQuery("SELECT h FROM Habit h WHERE h.name = :name", Habit.class)).thenReturn(typedQuery);

        Habit result = habitService.findHabitByName("Test Habit");

        assertEquals("Test Habit", result.getName());
        verify(entityManager, times(1)).createQuery("SELECT h FROM Habit h WHERE h.name = :name", Habit.class);
        verify(typedQuery, times(1)).setParameter("name", "Test Habit");
        verify(typedQuery, times(1)).getResultStream();
    }


    @Test
    void testFindHabitByName_NotFound() {
        TypedQuery<Habit> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Habit.class))).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.findHabitByName("Non-Existent Habit"));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Habit.class));
    }

}

