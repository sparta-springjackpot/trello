package com.example.trello.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.trello.dto.SigninRequestDto;
import com.example.trello.dto.SignupRequestDto;
import com.example.trello.entity.User;
import com.example.trello.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인/회원가입 비즈니스 로직")
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		String passwordConfirm = signupRequestDto.getPasswordConfirm();
		String nickname = signupRequestDto.getNickname();

		if (userRepository.findByUsername(username).isPresent()) {
			log.info("동일한 ID");
			throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
		} else if (userRepository.findByNickname(nickname).isPresent()) {
			log.info("동일한 닉네임");
			throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
		} else if (!signupRequestDto.getPassword().equals(passwordConfirm)) {
			log.info("비밀번호 불일치");
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		User user= new User(username, password, nickname);
			userRepository.save(user);
	}

	public void signin(SigninRequestDto signinRequestDto) {
		String username = signinRequestDto.getUsername();
		String password = signinRequestDto.getPassword();

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("등록되지 않은 회원입니다.")
		);

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

	}

}
