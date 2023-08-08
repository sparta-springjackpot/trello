package com.example.trello.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {

	private String nickname;
	private String password;
	private String passwordConfirm;

}
