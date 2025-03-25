package com.example.Library.model.basic;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)  // 'id' is the primary key, not nullable, and not updatable
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)  // 'username' is required, unique, max length of 50
    private String username;

    @Column(name = "password", nullable = false, length = 255)  // 'password' is required, max length of 255 characters (to accommodate hashed passwords)
    private String password;

    @Column(name = "role", nullable = false, length = 50)  // 'role' is required, max length of 50 characters
    private String role;

}