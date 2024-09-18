package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.History;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void saveProgressHistory(int userID, LocalDate date, double progress){
        History history = new History(userID, date, progress);
        entityManager.persist(history);
    }

    public History getProgressHistoryForUserAndDate(int userID, LocalDate date) {
        String query = "SELECT ph FROM History ph WHERE ph.userID = :userID AND ph.date = :date";
        try {
            return entityManager.createQuery(query, History.class)
                    .setParameter("userID", userID)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<History> getProgressHistoryForUser(int userID) {
        String query = "SELECT ph FROM History ph WHERE ph.userID = :userID";
        return entityManager.createQuery(query, History.class)
                .setParameter("userID", userID)
                .getResultList();
    }

}
