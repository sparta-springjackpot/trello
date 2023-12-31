package com.example.trello.entity;


import com.example.trello.dto.ProfileRequestDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column
	private String passwordConfirm;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column
	private String email;

	@Column
	private Long kakaoId;

//	@Column(nullable = false)
//	@Enumerated(value = EnumType.STRING) //이넘 타입 데이터를 주입 USER -> USER 그대로 저장
//	private UserRoleEnum role;

	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Board_User> board_userList = new ArrayList<>();

	public User(String username, String password, String nickname) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}

	public User(String username, String nickname, String encodedPassword, Long kakaoId) {
		this.username = username;
		this.nickname = nickname;
		this.password = encodedPassword;
		this.kakaoId = kakaoId;
	}

	// 동일 이메일로 가입을 시도할 경우, 카카오 아이디 갱신
	public User kakaoIdUpdate(Long kakaoId){
		this.kakaoId = kakaoId;
		return this;
	}
}