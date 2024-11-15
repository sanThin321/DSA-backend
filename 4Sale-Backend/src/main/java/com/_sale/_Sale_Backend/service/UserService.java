package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.exception.EmailSendException;
import com._sale._Sale_Backend.model.User;
import com._sale._Sale_Backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public String sendForgotPasswordCode(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "User not found with the provided email.";
        }

        // Generate a 6-digit verification code
        String code = generateVerificationCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(3);
        user.setVerificationCode(code);
        user.setVerificationExpiry(expiryTime);
        userRepo.save(user);

        String expiryFormatted = "3 minutes";

        try {
            emailService.sendVerificationEmail(email, "Your Verification Code", code, expiryFormatted);
            return "Verification code sent to email.";
        } catch (EmailSendException e) {
            return "Error sending verification email: " + e.getMessage();
        }
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public boolean verifyCode(String email, String code) {
        User user = userRepo.findByEmail(email);
        return user != null && user.getVerificationCode().equals(code) &&
                user.getVerificationExpiry().isAfter(LocalDateTime.now());
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email);
        if (user != null && user.getVerificationExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(encoder.encode(newPassword));
            user.setVerificationCode(null);
            user.setVerificationExpiry(null);
            userRepo.save(user);
        }
    }

    public String updateUsername(String email, String newUsername, String currentPassword) {
        User user = userRepo.findByEmail(email);

        // Check if user exists
        if (user == null) {
            return "User not found.";
        }

        // Verify the current password
        if (!encoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect.";
        }

        // Update the username
        user.setUsername(newUsername);
        userRepo.save(user);

        return "Username updated successfully.";
    }



    public String changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepo.findByEmail(email);

        // Check if user exists
        if (user == null) {
            return "User not found with the email.";
        }

        // Verify the current password
        if (!encoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect.";
        }

        // Update the password
        user.setPassword(encoder.encode(newPassword));
        user.setVerificationCode(null); // Clear verification details
        user.setVerificationExpiry(null);
        userRepo.save(user);

        return "Password changed successfully.";
    }


    public String changeEmail(String currentEmail, String newEmail, String currentPassword) {
        User user = userRepo.findByEmail(currentEmail);

        // Check if user exists
        if (user == null) {
            return "User not found with the provided current email.";
        }

        // Check if the current password is correct
        if (!encoder.matches(currentPassword, user.getPassword())) {
            return "Invalid current password.";
        }

        // Check if the new email is already in use
        if (userRepo.findByEmail(newEmail) != null) {
            return "The new email is already associated with another account.";
        }

        // Update the email
        user.setEmail(newEmail);
        userRepo.save(user);

        return "Email changed successfully.";
    }

}
