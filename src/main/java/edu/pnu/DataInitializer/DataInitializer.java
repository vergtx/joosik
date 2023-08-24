package edu.pnu.DataInitializer;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pnu.domain.Company;
import edu.pnu.domain.Member;
import edu.pnu.dto.PostRequest;
import edu.pnu.persistence.CompanyRepository;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.service.PostService;
import edu.pnu.service.PostServiceImpl;

//@Component
//public class DataInitializer implements CommandLineRunner {
//	
//	@Autowired
//    private PostService postService;
//
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private CompanyRepository companyRepository; // CompanyRepository 주입
//    
//
//    @Override
//    public void run(String... args) throws Exception {
//        initializeAdminUser();
//        initializeRandomUsers(10); // 10명의 랜덤 사용자 생성
//    }
//
//    private void initializeAdminUser() {
//        String adminUsername = "admin";
//        String adminPassword = "admin"; // 초기 비밀번호, 나중에 변경해야 함
//        String adminRole = "ROLE_ADMIN"; // ADMIN 역할
//
//        if (!memberRepository.existsByUsername(adminUsername)) {
//            Member adminMember = Member.builder()
//                    .username(adminUsername)
//                    .password(passwordEncoder.encode(adminPassword)) // PasswordEncoder 사용
//                    .role(adminRole)
//                    .enabled(true)
//                    .build();
//
//            memberRepository.save(adminMember);
//        }
//    }
//    
// 
//    private void initializeRandomUsers(int count) {
//        String userRole = "ROLE_USER";
//
//        for (int i = 0; i < count; i++) {
//            String username = "user" + i;
//            String password = "user" + i; // 초기 비밀번호, 나중에 변경해야 함
//
//            if (!memberRepository.existsByUsername(username)) {
//                Member userMember = Member.builder()
//                        .username(username)
//                        .password(passwordEncoder.encode(password))
//                        .role(userRole)
//                        .enabled(true)
//                        .build();
//
//                memberRepository.save(userMember);
//            }
//        }
//    }
//}

