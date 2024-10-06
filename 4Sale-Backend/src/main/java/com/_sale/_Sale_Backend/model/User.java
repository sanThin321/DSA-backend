package com._sale._Sale_Backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)  // Enforce email uniqueness
    private String email;

    @Column(nullable = false, unique = true)  // Enforce username uniqueness
    private String username;

    @Column(nullable = false)
    private String password;
}
