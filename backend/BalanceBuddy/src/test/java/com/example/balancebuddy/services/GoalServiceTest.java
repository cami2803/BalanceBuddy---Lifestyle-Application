package com.example.balancebuddy.services;

import com.example.balancebuddy.dtos.GoalRequestDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.enums.Periodicity;
import com.example.balancebuddy.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class GoalServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGoals() {
        TypedQuery<Goal> typedQuery = mock(TypedQuery.class);
        Goal goal = new Goal();

        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(goal));
        when(entityManager.createQuery("SELECT g from Goal g", Goal.class)).thenReturn(typedQuery);

        List<Goal> result = goalService.getAllGoals();

        assertEquals(Collections.singletonList(goal), result);
        verify(entityManager, times(1)).createQuery("SELECT g from Goal g", Goal.class);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    void testCreateGoal_UserNotFound() {
        GoalRequestDTO createGoalRequest = new GoalRequestDTO();
        createGoalRequest.setUserID(1);

        lenient().when(userService.findByID(createGoalRequest.getUserID())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> goalService.createGoal(createGoalRequest));
    }

    @Test
    void testUpdateGoal() {
        MyUser mockUser = new MyUser();
        mockUser.setUserID(1);

        Goal existingGoal = new Goal();
        existingGoal.setGoalID(1);
        existingGoal.setHabits("Exercise;Reading");
        existingGoal.setPeriodicity(Periodicity.DAILY);
        existingGoal.setTarget("10;5");
        existingGoal.setUser(mockUser);

        Goal updatedGoal = new Goal();
        updatedGoal.setGoalID(1);
        updatedGoal.setHabits("Exercise;Reading");
        updatedGoal.setPeriodicity(Periodicity.DAILY);
        updatedGoal.setTarget("15;10");
        updatedGoal.setUser(null);

        when(entityManager.find(Goal.class, 1)).thenReturn(existingGoal);
        when(entityManager.find(MyUser.class, 1)).thenReturn(mockUser);
        when(entityManager.merge(any(Goal.class))).thenReturn(updatedGoal);

        assertEquals(updatedGoal, goalService.updateGoal(1, updatedGoal));

        verify(entityManager).find(Goal.class, 1);
        verify(entityManager).find(MyUser.class, 1);
        verify(entityManager).merge(existingGoal);
    }

    @Test
    void testDeleteGoal() {
        Goal goal = new Goal();
        goal.setGoalID(1);
        goal.setHabits("exercise;diet");

        when(entityManager.find(Goal.class, 1)).thenReturn(goal);
        doNothing().when(entityManager).remove(goal);

        goalService.deleteGoal(1);
        verify(entityManager).remove(goal);
    }

    @Test
    void testAddHabitToGoal() {
        Goal goal = new Goal();
        goal.setGoalID(1);
        goal.setHabits("exercise;diet");

        when(entityManager.find(Goal.class, 1)).thenReturn(goal);
        when(entityManager.merge(any(Goal.class))).thenReturn(goal);

        Goal updatedGoal = goalService.addHabitToGoal(1, "newHabit", "targetValue");

        assertNotNull(updatedGoal);
        assertTrue(updatedGoal.getHabits().contains("newHabit"));
    }

    @Test
    void testUpdateProgress() {
        Goal goal = new Goal();
        goal.setGoalID(1);
        goal.setHabits("exercise;diet");
        goal.setProgress("0;0");

        when(entityManager.find(Goal.class, 1)).thenReturn(goal);
        when(entityManager.merge(any(Goal.class))).thenReturn(goal);

        goalService.updateProgress(1, "exercise", 10);

        assertEquals("10;0", goal.getProgress());
    }
}

