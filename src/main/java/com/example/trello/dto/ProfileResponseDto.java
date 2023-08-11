package com.example.trello.dto;

import com.example.trello.entity.User;

import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private String username;
    private String nickname;

    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
