package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.History;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private HistoryService historyService;

    @Mock
    private TypedQuery<History> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProgressHistory() {
        int userID = 1;
        LocalDate date = LocalDate.now();
        double progress = 75.5;

        historyService.saveProgressHistory(userID, date, progress);

        verify(entityManager, times(1)).persist(any(History.class));
    }

    @Test
    void testGetProgressHistoryForUserAndDate_Found() {
        int userID = 1;
        LocalDate date = LocalDate.now();
        History expectedHistory = new History(userID, date, 75.5);

        when(entityManager.createQuery(anyString(), eq(History.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("userID", userID)).thenReturn(typedQuery);
        when(typedQuery.setParameter("date", date)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedHistory);

        History result = historyService.getProgressHistoryForUserAndDate(userID, date);

        assertNotNull(result);
        assertEquals(expectedHistory, result);
        verify(entityManager, times(1)).createQuery(anyString(), eq(History.class));
    }

    @Test
    void testGetProgressHistoryForUserAndDate_NotFound() {
        int userID = 1;
        LocalDate date = LocalDate.now();

        when(entityManager.createQuery(anyString(), eq(History.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("userID", userID)).thenReturn(typedQuery);
        when(typedQuery.setParameter("date", date)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());

        History result = historyService.getProgressHistoryForUserAndDate(userID, date);

        assertNull(result);
        verify(entityManager, times(1)).createQuery(anyString(), eq(History.class));
    }

    @Test
    void testGetProgressHistoryForUser() {
        int userID = 1;
        List<History> expectedHistoryList = Collections.singletonList(new History(userID, LocalDate.now(), 80.0));

        when(entityManager.createQuery(anyString(), eq(History.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("userID", userID)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedHistoryList);

        List<History> result = historyService.getProgressHistoryForUser(userID);

        assertNotNull(result);
        assertEquals(expectedHistoryList.size(), result.size());
        assertEquals(expectedHistoryList, result);
        verify(entityManager, times(1)).createQuery(anyString(), eq(History.class));
    }
}
