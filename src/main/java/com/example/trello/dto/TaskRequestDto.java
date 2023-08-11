package com.example.trello.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequestDto {
    private String title;
    private LocalDateTime endDate;
}
