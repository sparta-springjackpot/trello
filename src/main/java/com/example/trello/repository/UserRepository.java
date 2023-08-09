package com.example.trello.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trello.entity.User;

public interface UserRepository extends JpaRepository <User, Long > {
    Optional <User> findByUsername(String username);
    Optional <User> findByNickname(String nickname);
}