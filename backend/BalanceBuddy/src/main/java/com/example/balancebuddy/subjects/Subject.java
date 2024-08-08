package com.example.balancebuddy.subjects;

import com.example.balancebuddy.observers.Observer;

public interface Subject {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
