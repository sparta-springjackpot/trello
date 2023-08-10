package com.example.trello.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.trello.dto.ProfileRequestDto;
import com.example.trello.dto.SigninRequestDto;
import com.example.trello.dto.SignupRequestDto;
import com.example.trello.entity.User;
import com.example.trello.jwt.JwtUtil;
import com.example.trello.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인/회원가입 비즈니스 로직")
@RequiredArgsConstructor
@Service
public class UserService {


	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

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

	public void signin(SigninRequestDto signinRequestDto, HttpServletResponse response) {
		String username = signinRequestDto.getUsername();
		String password = signinRequestDto.getPassword();

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("등록되지 않은 회원입니다.")
		);

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(signinRequestDto.getUsername()));
		jwtUtil.addJwtToCookie(response.getHeader(JwtUtil.AUTHORIZATION_HEADER), response);
	}

	public void modifiedProfile(ProfileRequestDto profileRequestDto, User user) {
		String nickname = profileRequestDto.getNickname();
		String password = passwordEncoder.encode(profileRequestDto.getPassword());
		String previousPassword = user.getPassword();

		if (userRepository.findByNickname(nickname).isPresent()) {
			throw new IllegalArgumentException("동일한 닉네임이 존재합니다.");
		} else if (passwordEncoder.matches(profileRequestDto.getPassword(), previousPassword)) {
			log.info("기존 비밀번호와 동일할 경우");
			throw new IllegalArgumentException("기존 비밀번호와 동일합니다.");
		}

		user.setNickname(nickname);
		user.setPassword(password);
		log.info("회원 정보 수정 시도");
		userRepository.save(user);
		log.info("회원 정보 수정 완료");
	}

	public void checkPassword(ProfileRequestDto requestDto, User user) {
		String password = user.getPassword();
		String passwordConfirm = requestDto.getPasswordConfirm();

		if (!passwordEncoder.matches(passwordConfirm, password)) {
			log.info("비밀번호 대조 실패");
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}
	}

}