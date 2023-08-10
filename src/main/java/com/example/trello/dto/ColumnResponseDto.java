package com.example.trello.dto;

import lombok.Data;

@Data
import java.util.List;

public class ColumnResponseDto {
    private int column_id;
    private String columnName;
    private Integer columnNumber;

    private List<CardResponseDto> cards;
}
