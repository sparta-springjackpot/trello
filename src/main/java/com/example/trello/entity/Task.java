package com.example.trello.entity;

import com.example.trello.dto.TaskRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @jakarta.persistence.Column(updatable = false)
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Task(TaskRequestDto requestDto, Card card) {
        this.title = requestDto.getTitle();
        this.endDate = requestDto.getEndDate();
        this.card = card;
    }
}
