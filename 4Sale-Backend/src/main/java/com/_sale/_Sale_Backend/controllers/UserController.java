package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.User;
import com._sale._Sale_Backend.model.dto.UserResponseDTO;
import com._sale._Sale_Backend.service.JwtService;
import com._sale._Sale_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String code) {

        // Verify the verification code
        if (!userService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code.");
        }

        // Attempt to change the password
        String response = userService.changePassword(email, currentPassword, newPassword);
        if (response.equals("Password changed successfully.")) {
            return ResponseEntity.ok(response);
        }

        // Handle failures
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                // Generate the JWT token
                String token = jwt.generateToken(loginRequest.getUsername());

                // Retrieve the user from the database
                User user = userService.findUserByUsername(loginRequest.getUsername());
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
                }

                UserResponseDTO userResponseDTO = new UserResponseDTO(user);

                // Create the response object
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", userResponseDTO); // Add the user object (ensure no sensitive info like password is exposed)

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
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

    @PostMapping("/change-username")
    public ResponseEntity<?> updateUsername(
            @RequestParam String email,
            @RequestParam String newUsername,
            @RequestParam String currentPassword) {

        // Update username
        String response = userService.updateUsername(email, newUsername, currentPassword);

        if (!response.equals("Username updated successfully.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Generate new token with the updated username
        String newToken = jwt.generateToken(newUsername);

        // Prepare the updated user response
        User updatedUser = userService.findByEmail(email);
        UserResponseDTO userResponseDTO = new UserResponseDTO(updatedUser);

        // Create the response object
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", response);
        responseBody.put("token", newToken);
        responseBody.put("user", userResponseDTO);

        return ResponseEntity.ok(responseBody);
    }



    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(
            @RequestParam String currentEmail,
            @RequestParam String newEmail,
            @RequestParam String currentPassword) {
        String response = userService.changeEmail(currentEmail, newEmail, currentPassword);

        // Return appropriate HTTP status based on the response
        return switch (response) {
            case "Email changed successfully." -> ResponseEntity.status(HttpStatus.OK).body(response);
            case "User not found with the provided current email." ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            case "Invalid current password.", "The new email is already associated with another account." ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }


}
