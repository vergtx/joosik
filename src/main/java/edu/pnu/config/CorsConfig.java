//package edu.pnu.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//// 다른 컴퓨터에서 내 ip 넣어서 내 boot 끌어다 쓸때 필요함.
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // /company/** 경로에 대해서만 CORS 설정 적용
//            .allowedOrigins("http://localhost:3000") // React 애플리케이션 주소
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드 설정
//            .allowedHeaders("*") // 허용할 헤더 설정
//            .allowCredentials(true); // 인증정보(쿠키, 인증헤더)를 사용할지 여부
//    }
//}
