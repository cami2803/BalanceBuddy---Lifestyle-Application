package com.example.balancebuddy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habitid")
    private int habitID;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "unit")
    private String unit;

    public Habit(){

    }

    public int getHabitID() {
        return habitID;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void unit(String unit) {
        this.unit = unit;
    }
}
