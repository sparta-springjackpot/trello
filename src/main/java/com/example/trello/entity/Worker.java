//package com.example.trello.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.userdetails.User;
//
//@Getter
//@Entity
//@NoArgsConstructor
//public class Worker {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "card_id")
//    private Card card;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    public Worker(Card card, User newWorker) {
//        this.card = card;
//        this.user = newWorker;
//    }
//}
