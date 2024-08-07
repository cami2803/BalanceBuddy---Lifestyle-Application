package com.example.balancebuddy.subjects;

import com.example.balancebuddy.observers.Observer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserActivity implements Subject {

    private List<Observer> observers = new ArrayList<>();

    private String habitData;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(habitData);
        }
    }

    public void setHabitData(String habitData) {
        this.habitData = habitData;
        notifyObservers();
    }

    public String getHabitData() {
        return habitData;
    }
}
