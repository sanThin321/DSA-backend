package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.User;
import com._sale._Sale_Backend.service.JwtService;
import com._sale._Sale_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwt;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        return userService.sendForgotPasswordCode(email);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwt.generateToken(user.getUsername());
        } else {
            return "Login Failed.";
        }
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = userService.verifyCode(email, code);
        return isValid ? "Verification successful" : "Invalid or expired code";
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword) {

        // Check if the code is valid
        if (userService.verifyCode(email, code)) {
            userService.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }
    }

    @PostMapping("/update-username")
    public ResponseEntity<String> updateUsername(
            @RequestParam String oldUserName,
            @RequestParam String newUserName) {

        userService.updateUsername(oldUserName, newUserName);
        return ResponseEntity.status(HttpStatus.OK).body("Username updated successfully.");
    }

}
