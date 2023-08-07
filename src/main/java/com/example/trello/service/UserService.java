package com.example.trello.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.trello.dto.SigninRequestDto;
import com.example.trello.dto.SignupRequestDto;
import com.example.trello.entity.User;
import com.example.trello.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		String nickname = signupRequestDto.getNickname();

		if (userRepository.findByUsername(username).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
		} else if (userRepository.findByNickname(nickname).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
		} else if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		User user= new User(username, password, nickname);
			userRepository.save(user);
	}

	public void signin(SigninRequestDto signinRequestDto) {


	}

}
