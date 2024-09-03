package com.example.balancebuddy.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressData {

    // key: habit name
    // value: progress value
    private Map<String, Integer> progress;

    // key: habit name
    // value: target value
    private Map<String, Integer> target;

    // key: habit name
    // value: unit value
    private Map<String, String> unit;

    public boolean hasProgress(){
        return !progress.isEmpty();
    }
}
