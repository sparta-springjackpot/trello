package com.example.trello.dto;

import lombok.Data;

@Data
public class ColumnRequestDto {
    private String columnName;
    private Integer columnNumber;
}
