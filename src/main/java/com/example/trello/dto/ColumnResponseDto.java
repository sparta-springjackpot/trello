package com.example.trello.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColumnResponseDto {
    private Long column_id;
    private String columnName;
    private Integer columnNumber;

    private List<CardResponseDto> cards;
}
