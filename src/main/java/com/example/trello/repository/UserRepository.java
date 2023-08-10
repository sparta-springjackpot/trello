//package com.example.trello.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    Optional<User> findByUsername(String username);
//
//    Optional<User> findByEmail(String email);
//}