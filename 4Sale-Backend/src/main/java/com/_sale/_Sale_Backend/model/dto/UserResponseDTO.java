package com._sale._Sale_Backend.model.dto;

import com._sale._Sale_Backend.model.User;

public class UserResponseDTO {
    private int id;
    private String email;
    private String username;

    // Constructor
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
