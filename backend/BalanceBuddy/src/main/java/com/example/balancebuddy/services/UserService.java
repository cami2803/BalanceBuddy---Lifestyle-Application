package com.example.balancebuddy.services;

import com.example.balancebuddy.entities.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<MyUser> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM MyUser u", MyUser.class).getResultList();
    }

    @Transactional
    public MyUser createUser(MyUser user){
        entityManager.persist(user);
        return user;
    }

    @Transactional
    public Optional<MyUser> findByID(Integer id) {
        return Optional.ofNullable(entityManager.find(MyUser.class, id));
    }

    @Transactional
    public Optional<MyUser> findByEmail(String email) {
        TypedQuery<MyUser> query = entityManager.createQuery("SELECT u FROM MyUser u WHERE u.email = :email", MyUser.class);
        query.setParameter("email", email);
        List<MyUser> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

}
