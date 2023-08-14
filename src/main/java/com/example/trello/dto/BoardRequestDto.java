package com.example.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {
    private String board_name;
    private String back_color;
    private String introduce;
}
