package com.example.balancebuddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goalID")
    private int goalID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private MyUser user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periodicity")
    private Periodicity periodicity;

    @NotNull
    @Column(name = "target")
    private String target; // Target values are stored as a semicolon-separated string

    @NotNull
    @Column(name = "habits")
    private String habits; // Habits are stored as a semicolon-separated string

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }
}
