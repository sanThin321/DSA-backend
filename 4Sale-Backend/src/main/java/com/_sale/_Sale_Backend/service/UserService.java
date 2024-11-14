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

    // update user name
    public void updateUsername(String oldUserName, String newUserName) {
        User user = userRepo.findByUsername(oldUserName);

        if (user == null) {
           return;
        }

        user.setUsername(newUserName);
        userRepo.save(user);
    }

}
