package com.example.trello.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;

}
