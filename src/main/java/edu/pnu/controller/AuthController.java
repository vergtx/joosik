package edu.pnu.controller;


import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import edu.pnu.domain.Member;
import edu.pnu.dto.ApiResponse;

import edu.pnu.dto.SignupRequest;
import edu.pnu.persistence.MemberRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;


	public AuthController(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		// 사용자명이 이미 사용 중인지 확인
		if (memberRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "사용자명이 이미 사용 중입니다."));
		}

		// 새로운 사용자 생성
		Member member = new Member(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()),
				"ROLE_USER", true);

		memberRepository.save(member);

		return ResponseEntity.ok(new ApiResponse(true, "사용자가 성공적으로 등록되었습니다."));
	}

	
	// 다른 API 메서드 (로그인 등) 추가 가능
}
