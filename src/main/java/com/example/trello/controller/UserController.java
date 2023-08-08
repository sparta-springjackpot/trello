package com.example.trello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trello.dto.ApiResponseDto;
import com.example.trello.dto.SigninRequestDto;
import com.example.trello.dto.SignupRequestDto;
import com.example.trello.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/user/signup")
	public ResponseEntity<ApiResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
		try {
			userService.signup(signupRequestDto);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.ok().body(new ApiResponseDto("회원가입에 실패했습니다.", HttpStatus.BAD_REQUEST.value()));
		}
		return ResponseEntity.ok().body(new ApiResponseDto("회원가입에 성공했습니다.", HttpStatus.CREATED.value()));
	}

	@PostMapping("/user/login")
	public ResponseEntity<ApiResponseDto> login(@RequestBody SigninRequestDto signinRequestDto, HttpServletResponse res) {
		try {
			userService.signin(signinRequestDto, res);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.ok().body(new ApiResponseDto("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST.value()));
		}
		return ResponseEntity.ok().body(new ApiResponseDto("로그인에 성공했습니다.", HttpStatus.CREATED.value()));
	}

}
