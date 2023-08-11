package com.example.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiResponseDto {

    private int status;
    private String success;
    private Object result;

    public RestApiResponseDto(int status, String success, Object result) {
        this.status = status;
        this.success = success;
        this.result = result;
    }

    public RestApiResponseDto(int status, String success) {
        this.status = status;
        this.success = success;
    }
}
