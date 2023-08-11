package com.example.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiResponseDto {

    private int status;
    private String msg;
    private Object result;

    public RestApiResponseDto(int status, String msg, Object result) {
        this.status = status;
        this.msg = msg;
        this.result = result;
    }

    public RestApiResponseDto(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
