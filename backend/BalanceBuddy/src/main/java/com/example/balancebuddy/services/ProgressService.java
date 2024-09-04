package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.entities.ProgressData;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final HabitService habitService;

    private final GoalService goalService;

    @Cacheable(value = "dailyUserProgress", key = "#userID")
    public ProgressData getProgressForUser(Integer userID) {
        List<Goal> goals = goalService.findGoalsByUserId(userID);

        Map<String, Integer> progressMap = new HashMap<>();
        Map<String, Integer> targetMap = new HashMap<>();
        Map<String, String> unitMap = new HashMap<>();

        for (Goal goal : goals) {
            String[] habitsArray = goal.getHabits().split(";");
            String[] targetArray = goal.getTarget().split(";");
            String[] progressArray = goal.getProgress().split(";");

            for (int i = 0; i < habitsArray.length; i++) {
                String habitName = habitsArray[i];
                Integer targetValue = Integer.parseInt(targetArray[i]);
                Integer progressValue = Integer.parseInt(progressArray[i]);

                Habit habit = habitService.findHabitByName(habitName);
                String unit = habit != null ? habit.getUnit() : "units";

                progressMap.put(habitName, progressMap.getOrDefault(habitName, 0) + progressValue);
                targetMap.put(habitName, targetMap.getOrDefault(habitName, targetValue));
                unitMap.put(habitName, unit);
            }
        }
        return new ProgressData(progressMap, targetMap, unitMap);
    }

}
