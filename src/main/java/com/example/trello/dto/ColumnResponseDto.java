package com.example.trello.dto;

import lombok.Data;

@Data
public class ColumnResponseDto {
    private Long id;
    private String columnName;
    private Integer columnNumber;
}
