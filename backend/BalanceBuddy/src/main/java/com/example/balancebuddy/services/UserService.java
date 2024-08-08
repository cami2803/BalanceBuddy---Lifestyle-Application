package com.example.balancebuddy.services;

import com.example.balancebuddy.dtos.NotificationSettingsDTO;
import com.example.balancebuddy.entities.MyUser;
import com.example.balancebuddy.enums.Role;
import com.example.balancebuddy.exceptions.PasswordChangeException;
import com.example.balancebuddy.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<MyUser> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM MyUser u", MyUser.class).getResultList();
    }

    @Transactional
    public Optional<MyUser> findByID(Integer id) {
        MyUser user = entityManager.find(MyUser.class, id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return Optional.of(user);
    }

    @Transactional
    public Optional<MyUser> findByEmail(String email) {
        TypedQuery<MyUser> query = entityManager.createQuery("SELECT u FROM MyUser u WHERE u.email = :email", MyUser.class);
        query.setParameter("email", email);
        List<MyUser> result = query.getResultList();
        if (result.isEmpty()) {
            throw new UserNotFoundException(email);
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Transactional
    public void myChangePassword(String email, String oldPassword, String newPassword) throws Exception {
        Optional<MyUser> userOptional = findByEmail(email);

        // If the user exists in the database, continue changing the passwords
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                if (oldPassword.equals(newPassword)) {
                    throw new Exception("New password cannot be the same as the old password");
                }
                user.setPassword(passwordEncoder.encode(newPassword));
                entityManager.merge(user);
            } else {
                throw new PasswordChangeException("Old password is incorrect!");
            }
        } else {
            throw new UserNotFoundException(email);
        }
    }

    public boolean userExists(String email) {
        return findByEmail(email).isPresent();
    }

    public MyUser loadUserByUsername(String email) throws UserNotFoundException {
        Optional<MyUser> userOptional = findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(email);
        }
        return userOptional.get();
    }

    @Transactional
    @Override
    public void createUser(UserDetails userDetails) {
        MyUser myUser = (MyUser) userDetails;
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setRole(Role.USER);
        myUser.setReminder(true);
        myUser.setDaily(true);
        entityManager.persist(myUser);
    }

    @Transactional
    @Override
    public void updateUser(UserDetails userDetails) {
        MyUser myUser = (MyUser) userDetails;
        MyUser existingUser = entityManager.find(MyUser.class, myUser.getUserID());

        if (existingUser != null) {
            existingUser.setFirstname(myUser.getFirstname());
            existingUser.setLastname(myUser.getLastname());
            existingUser.setEmail(myUser.getEmail());
            existingUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            existingUser.setDaily(myUser.isDaily());
            existingUser.setReminder(myUser.isReminder());
            entityManager.merge(existingUser);
        } else {
            throw new UserNotFoundException(myUser.getUserID());
        }
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        Optional<MyUser> userOptional = findByEmail(email);
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            try {
                entityManager.remove(user);
            } catch (Exception e) {
                throw new UserNotFoundException(email);
            }
        } else {
            throw new UserNotFoundException(email);
        }
    }


    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Transactional
    public void updateNotificationSettings(int userID, NotificationSettingsDTO settingsDTO) {
        MyUser user = entityManager.find(MyUser.class, userID);
        if (user == null) {
            throw new UserNotFoundException(userID);
        }

        user.setDaily(settingsDTO.isDaily());
        user.setReminder(settingsDTO.isReminder());

        entityManager.merge(user);
    }

    @Transactional
    public NotificationSettingsDTO getNotificationSettings(int userID) {
        MyUser user = entityManager.find(MyUser.class, userID);
        if (user == null) {
            throw new UserNotFoundException(userID);
        }

        NotificationSettingsDTO settingsDTO = new NotificationSettingsDTO();
        settingsDTO.setDaily(user.isDaily());
        settingsDTO.setReminder(user.isReminder());

        return settingsDTO;
    }

}
