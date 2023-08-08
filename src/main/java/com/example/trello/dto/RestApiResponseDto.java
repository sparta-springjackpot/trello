package com.example.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestApiResponseDto {
    private int status;
    private String success;
    private Object result;
}
