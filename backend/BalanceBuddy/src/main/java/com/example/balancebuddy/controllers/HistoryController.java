package com.example.balancebuddy.controllers;

import com.example.balancebuddy.entities.History;
import com.example.balancebuddy.services.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService progressHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<List<History>> getUserProgressHistory(@PathVariable int id) {
        List<History> history = progressHistoryService.getProgressHistoryForUser(id);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}/{date}")
    public ResponseEntity<History> getProgressForSpecificDate(
            @PathVariable int id,
            @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        History history = progressHistoryService.getProgressHistoryForUserAndDate(id, localDate);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
