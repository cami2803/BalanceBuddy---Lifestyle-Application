package com.example.balancebuddy.services;

import com.example.balancebuddy.dtos.GoalRequestDTO;
import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserService userService;

    @Transactional
    public List<Goal> getAllGoals() {
        return entityManager.createQuery("SELECT g from Goal g", Goal.class).getResultList();
    }

    @Transactional
    public Goal createGoal(GoalRequestDTO createGoalRequest) {
        // Use UserService to find the user by ID
        Optional<MyUser> userOptional = userService.findByID(createGoalRequest.getUserID());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        MyUser user = userOptional.get();

        Goal goal = Goal.builder()
                .user(user) // Set the user entity
                .periodicity(createGoalRequest.getPeriodicity())
                .target(createGoalRequest.getTarget())
                .habits(createGoalRequest.getHabits())
                .build();

        entityManager.persist(goal);
        return goal;
    }

    @Transactional
    public Optional<Goal> findGoalByID(int id) {
        return Optional.ofNullable(entityManager.find(Goal.class, id));
    }

    @Transactional
    public Goal updateGoal(int id, Goal updatedGoal) {
        Goal existingGoal = entityManager.find(Goal.class, id);
        if (existingGoal == null) {
            throw new RuntimeException("Goal not found!");
        }

        existingGoal.setPeriodicity(updatedGoal.getPeriodicity());
        existingGoal.setTarget(updatedGoal.getTarget());
        existingGoal.setHabits(updatedGoal.getHabits());

        existingGoal.setUser(existingGoal.getUser());
        return entityManager.merge(existingGoal);
    }

    @Transactional
    public void deleteGoal(int id) {
        Goal goal = entityManager.find(Goal.class, id);
        if (goal != null) {
            entityManager.remove(goal);
        }
    }

    @Transactional
    public Goal addHabitToGoal(int goalID, String habitName, String targetValue) {
        Goal goal = entityManager.find(Goal.class, goalID);
        if (goal != null) {
            String currentHabits = goal.getHabits();
            if (currentHabits == null || currentHabits.isEmpty()) {
                goal.setHabits(habitName + ";");
            } else {
                goal.setHabits(currentHabits + ";" + habitName);
            }

            String currentTargetValues = goal.getTarget();
            if (currentTargetValues == null || currentTargetValues.isEmpty()) {
                goal.setTarget(targetValue + ";");
            } else {
                goal.setTarget(currentTargetValues + ";" + targetValue);
            }

            entityManager.merge(goal);
        }
        return goal;
    }
}
