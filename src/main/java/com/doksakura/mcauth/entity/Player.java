package com.doksakura.mcauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Player {
    @Id
    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private LocalDateTime lastLogin;
}