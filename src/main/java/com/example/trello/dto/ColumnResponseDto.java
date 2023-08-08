package com.example.trello.dto;

import lombok.Data;

@Data
public class ColumnResponseDto {
    private int column_id;
    private String columnName;
    private Integer columnNumber;
}
