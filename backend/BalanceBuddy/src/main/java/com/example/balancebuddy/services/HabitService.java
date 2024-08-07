package com.example.balancebuddy.services;

import com.example.balancebuddy.dtos.HabitRequestDTO;
import com.example.balancebuddy.entities.Habit;
import com.example.balancebuddy.exceptions.HabitNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<Habit> getAllHabits(){
        return entityManager.createQuery("SELECT h from Habit h", Habit.class).getResultList();
    }

    @Transactional
    public Habit createHabit(HabitRequestDTO habitRequestDTO) {
        Habit habit = Habit.builder()
                .name(habitRequestDTO.getName())
                .unit(habitRequestDTO.getUnit())
                .build();

        entityManager.persist(habit);
        return habit;
    }

    @Transactional
    public Optional<Habit> findHabitByID(int id){
        Habit habit = entityManager.find(Habit.class, id);
        if (habit == null) {
            throw new HabitNotFoundException(id);
        }
        return Optional.of(habit);
    }

    @Transactional
    public Habit updateHabit(Habit habit){
        return entityManager.merge(habit);
    }

    @Transactional
    public void deleteHabit(int id){
        Habit habit = entityManager.find(Habit.class, id);
        if (habit != null){
            entityManager.remove(habit);
        } else {
            throw new HabitNotFoundException(id);
        }
    }

    @Transactional
    public Habit findHabitByName(String name) {
        TypedQuery<Habit> query = entityManager.createQuery("SELECT h FROM Habit h WHERE h.name = :name", Habit.class);
        query.setParameter("name", name);
        Habit habit = query.getResultStream().findFirst().orElse(null);
        if (habit == null) {
            throw new HabitNotFoundException(name);
        }
        return habit;
    }
}
