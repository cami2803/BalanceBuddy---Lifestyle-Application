package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.Goal;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.subjects.UserActivity;
import com.example.balancebuddy.utils.NotificationUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTasksService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationUtils notificationUtils;

    @Autowired
    private UserActivity userActivity;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;

    @PostConstruct
    public void init() {
        // Register observers
        userActivity.addObserver(notificationService);
        userActivity.addObserver(reportGeneratorService);
    }

    // Send notifications hourly
    @Scheduled(cron = "0 0 * * * *")
    // @Scheduled(cron = "*/30 * * * * *") // Testing - send notifications every 30 seconds
    public void sendPeriodicNotifications() {
        List<MyUser> users = userService.getAllUsers();
        for (MyUser user : users) {
            if (user.isReminder()) { // If the user has progress notifications on
                // Send periodic notifications
                String message = "Don't forget about your goal!";
                notificationUtils.sendNotification(user.getEmail(), message);
            }
        }
    }

    // Scheduled to run daily at 10 PM
    @Scheduled(cron = "0 0 22 * * ?")
    public void generateDailyReports() {
        List<MyUser> users = userService.getAllUsers();
        for (MyUser user : users) {
            if (user.isDaily()) { // If the user has daily report notifications on
                // Generate report
                String report = generateReportForUser(user);
                notificationUtils.sendNotification(user.getEmail(), report);
            }
        }
    }

    private String generateReportForUser(MyUser user) {
        List<Goal> goals = goalService.getAllGoals();
        StringBuilder report = new StringBuilder("Your daily report:\n");

        boolean hasGoals = false; // Flag to check if any goals were found

        for (Goal goal : goals) {
            if (goal.getUser().equals(user)) {
                hasGoals = true;
                int progressPercentage = computeGoalProgress(goal);

                // Split habits and target values
                String[] habitsArray = goal.getHabits().split(";");
                String[] progressArray = goal.getProgress().split(";");
                String[] targetArray = goal.getTarget().split(";");

                // Generate report details for each habit
                for (int i = 0; i < habitsArray.length; i++) {
                    String habitName = habitsArray[i];
                    String target = targetArray[i];
                    String progress = progressArray[i];

                    // Fetch habit details including unit
                    Habit habit = habitService.findHabitByName(habitName);
                    String unit = habit != null ? habit.getUnit() : "units";

                    report.append(String.format("Habit: %s\n", habitName));
                    report.append(String.format("Target: %s %s\n", target, unit));
                    report.append(String.format("Progress: %s %s\n\n", progress, unit));
                }

                // Summary of goal progress
                report.append(String.format("Overall Progress: %d%%\n\n", progressPercentage));
            }
        }

        if (!hasGoals) {
            report.append("You have no goals for today.\n");
        }

        return report.toString();
    }


    private int computeGoalProgress(Goal goal) {
        String[] progressArray = goal.getProgress().split(";");
        String[] targetArray = goal.getTarget().split(";");

        int totalTarget = 0;
        int totalProgress = 0;

        for (int i = 0; i < progressArray.length; i++) {
            totalTarget += Integer.parseInt(targetArray[i]);
            totalProgress += Integer.parseInt(progressArray[i]);
        }

        // In case of division by 0
        if (totalTarget == 0) return 0;

        return (totalProgress * 100) / totalTarget;
    }
}
