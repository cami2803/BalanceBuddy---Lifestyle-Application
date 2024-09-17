package com.example.balancebuddy.controllers;

import com.example.balancebuddy.entities.History;
import com.example.balancebuddy.services.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class HistoryControllerTest {

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProgressHistory() {
        List<History> historyList = Collections.singletonList(new History(1, LocalDate.now(), 75.0));
        when(historyService.getProgressHistoryForUser(1)).thenReturn(historyList);

        ResponseEntity<List<History>> response = historyController.getUserProgressHistory(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(historyList, response.getBody());
    }

    @Test
    void testGetProgressForSpecificDate() {
        LocalDate date = LocalDate.now();
        History history = new History(1, date, 90.0);
        when(historyService.getProgressHistoryForUserAndDate(1, date)).thenReturn(history);

        ResponseEntity<History> response = historyController.getProgressForSpecificDate(1, date.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(history, response.getBody());
    }

    @Test
    void testGetProgressForSpecificDate_NotFound() {
        LocalDate date = LocalDate.now();
        when(historyService.getProgressHistoryForUserAndDate(1, date)).thenReturn(null);

        ResponseEntity<History> response = historyController.getProgressForSpecificDate(1, date.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
