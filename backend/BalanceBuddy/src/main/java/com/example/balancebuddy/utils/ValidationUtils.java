package com.example.balancebuddy.utils;

import com.example.balancebuddy.dtos.ChangePasswordDTO;
import com.example.balancebuddy.entities.Login;
import com.example.balancebuddy.entities.SignUp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    // Password must have:
    // - at least: 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number
    // - can contain special characters
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    public static List<String> validateSignUp(SignUp signUp) {
        List<String> errors = new ArrayList<>();

        if (signUp.getEmail() == null || !Pattern.matches(EMAIL_PATTERN, signUp.getEmail())) {
            errors.add("Invalid email format");
        }
        if (signUp.getFirstname() == null || signUp.getFirstname().length() < 1 || signUp.getFirstname().length() > 30) {
            errors.add("Firstname must be between 1 and 30 characters");
        }
        if (signUp.getLastname() == null || signUp.getLastname().length() < 1 || signUp.getLastname().length() > 30) {
            errors.add("Lastname must be between 1 and 30 characters");
        }
        if (signUp.getPassword() == null){
            errors.add("Password field cannot be empty");
        }
        if(!Pattern.matches(PASSWORD_PATTERN, signUp.getPassword())) {
            errors.add("Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character");
        }

        return errors;
    }

    public static List<String> validateLogin(Login login) {
        List<String> errors = new ArrayList<>();

        if (login.getEmail() == null || !Pattern.matches(EMAIL_PATTERN, login.getEmail())) {
            errors.add("Invalid email format");
        }
        if (login.getPassword() == null){
            errors.add("Password field cannot be empty");
        }
        if(!Pattern.matches(PASSWORD_PATTERN, login.getPassword())) {
            errors.add("Incorrect password");
        }

        return errors;
    }

    public static List<String> validateChangePassword(ChangePasswordDTO changePasswordDTO){
        List<String> errors = new ArrayList<>();

        if(changePasswordDTO.getEmail() == null || !Pattern.matches(EMAIL_PATTERN, changePasswordDTO.getEmail())) {
            errors.add("Invalid email format");
        }
        if(changePasswordDTO.getNewPassword() == null) {
            errors.add("New password field cannot be empty");
        }
        if(!Pattern.matches(PASSWORD_PATTERN, changePasswordDTO.getNewPassword())){
            errors.add("New password must be at least 8 characters long, contain an uppercase letter, a number, and a special character");
        }
        if(changePasswordDTO.getOldPassword() == null){
            errors.add("Old password field cannot be empty");
        }

        return errors;
    }
}
