package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.entities.ProgressData;
import com.example.balancebuddy.subjects.UserActivity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasksService.class);

    private final UserService userService;

    private final UserActivity userActivity;

    private final NotificationService notificationService;

    private final ReportGeneratorService reportGeneratorService;

    private final ProgressService progressService;

    private final HistoryService historyService;

    @PostConstruct
    public void init() {
        // Register observers
        userActivity.addObserver(notificationService);
        userActivity.addObserver(reportGeneratorService);
    }

    // Send notifications hourly
    @Scheduled(cron = "0 0 * * * *")
    @Async
    public void sendPeriodicNotifications() {
        List<MyUser> users = userService.getAllUsers();
        List<String> notifications = users.stream()
                .filter(MyUser::isReminder)
                .map(user -> {
                    String message = "Don't forget about your goal!";
                    return "Notification for " + user.getUsername() + ": " + message;
                }).toList();

        notifications.forEach(System.out::println);
    }

    // Send report at 10PM everyday
    @Scheduled(cron = "0 0 22 * * ?")
    @Async
    public void generateDailyReports() {
        List<MyUser> users = userService.getAllUsers();
        List<String> reports = users.stream()
                .filter(MyUser::isDaily)
                .map(user -> {
                    ProgressData progressData = progressService.getProgressForUser(user.getUserID());
                    double overallProgress = calculateOverallProgress(progressData);

                    double roundedProgress = Math.round(overallProgress * 100.0) / 100.0;
                    historyService.saveProgressHistory(user.getUserID(), LocalDate.now(), roundedProgress);

                    String report = generateReportForUser(user, progressData);
                    return "Daily report for " + user.getUsername() + ":\n" + report;
                }).toList();

        reports.forEach(System.out::println);
    }


    private String generateReportForUser(MyUser user, ProgressData progressData) {
        StringBuilder report = new StringBuilder("Your daily report:\n");

        if (progressData.hasProgress()) {
            logger.info("User {} has progress data", user.getUsername());
            for (String habit : progressData.getProgress().keySet()) {
                int progressValue = progressData.getProgress().get(habit);
                int targetValue = progressData.getTarget().get(habit);
                String unit = progressData.getUnit().get(habit);

                logger.info("Habit: {}, Target: {}, Progress: {}", habit, targetValue, progressValue);

                report.append(String.format("Habit: %s\n", habit));
                report.append(String.format("Target: %d %s\n", targetValue, unit));
                report.append(String.format("Progress: %d %s\n\n", progressValue, unit));
            }

            double overallProgress = calculateOverallProgress(progressData);
            report.append(String.format("Overall Progress: %.2f%%\n\n", overallProgress));
        } else {
            logger.info("User {} has no progress data", user.getUsername());
            report.append("You have no goals for today.\n");
        }

        return report.toString();
    }


    private double calculateOverallProgress(ProgressData progressData) {
        int totalTarget = progressData.getTarget().values().stream().mapToInt(Integer::intValue).sum();
        int totalProgress = progressData.getProgress().values().stream().mapToInt(Integer::intValue).sum();

        return totalTarget > 0 ? (totalProgress * 100.0) / totalTarget : 0;
    }

}
