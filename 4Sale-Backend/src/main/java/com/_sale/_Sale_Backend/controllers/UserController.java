package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.User;
import com._sale._Sale_Backend.service.JwtService;
import com._sale._Sale_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("register")
    public User register(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("login")
    public String login(@RequestBody User user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwt.generateToken(user.getUsername());
        } else {
            return "Login Failed.";
        }
    }

    @GetMapping("/auth/user")
    public User getUser(@RequestHeader("Authorization") String token) {
        // Remove the "Bearer " prefix from the token
        token = token.substring(7);

        // Extract the username from the token
        String username = jwt.extractUserName(token);

        // Retrieve and return user details based on the username
        return userService.findUserByUsername(username);
    }
}
