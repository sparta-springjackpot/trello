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
@Table(name = "task")
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

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Columns columns;

    public Task(TaskRequestDto requestDto, Card card, Columns columns) {
        this.title = requestDto.getTitle();
        this.endDate = requestDto.getEndDate();
        this.card = card;
        this.columns = columns;
    }
}
