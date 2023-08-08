package com.example.trello.entity;

import jakarta.persistence.*;

@Entity
public class Board_User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
}
