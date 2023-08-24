package edu.pnu.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pnu.config.auth.JWTAuthorizationFilter;
import edu.pnu.config.filter.JWTAuthenticationFilter;
import edu.pnu.persistence.MemberRepository;

@Configuration //설정 클래스 인식시키기
@EnableWebSecurity //스프링 시큐리티 설정 활성화. 웹 보안관련 설정 기본값 자동적용
public class SecurityConfig {
	
	@Bean //컨테이너에 자동 빈 등록
	public PasswordEncoder passwordEncoder() { //비번 암호화 및 비교 인터페이스.
		return new BCryptPasswordEncoder();
	}

	@Bean // 0807 추가. 프론트에서 로그인. CORS 설정위한 메소드. 다른 도메인의 요청 허용을 사용자가 설정.
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration(); //CORS 설정위한 객체 생성
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용 도메인(출처, origin) 설정. 3000만 허용.
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); //options는 왜있는겨. 지워.
		configuration.setAllowedHeaders(Arrays.asList("*")); //모든 헤더 허용
		configuration.setAllowCredentials(true); //자격증명(쿠키, HTTP인증) 요청 허용.

		configuration.addExposedHeader("Authorization"); // 이거~ 클라이언트에서 Authorization 헤더값 읽을수 있게 해준다.
		//이걸 해줬기때문에 클라이언트 쪽에서 Bearer~ 이걸 읽을수 있었겠지

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //URL 기반의 CORS 설정을 위한 객체생성.
		source.registerCorsConfiguration("/**", configuration); //모든 URL(/**)에 대해 위에서 정의한 configuration의 CORS 설정을 적용합니다.
		return source; //설정된 UrlBasedCorsConfigurationSource 객체를 반환. 이 반환 값은 Spring Security의 CORS 필터에서 사용.
	}

	@Autowired //스프링 컨테이너로부터 자동 주입. 인증 설정 담당.
	private AuthenticationConfiguration authConfig;

	@Autowired
	private MemberRepository memberRepo;

//	@SuppressWarnings("removal")
	@Autowired

//	@Qualifier("corsConfigurationSource") //이거 없으면 @Bean에 2개가 올라가서 뭘 골라야 할지 모른다. WebConfig에 있는걸 고르게 하자.
//	private CorsConfigurationSource corsConfigurationSource

	@Bean //보안필터 체인 설정
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//cross-site Request Forgery(공격방지 설정. CSRF 보호기능 비활성화)
		http.csrf(csrf -> csrf.disable());
		/// http.cors(cors -> cors.disable());
//		http.cors();
//		http.cors(Customizer.withDefaults()); //사용하고 싶을때 이제 7.0 버전에선 이걸 쓰는데

		http.cors(cors -> {
			cors.configurationSource(corsConfigurationSource());
		}); //위에서 이걸 쓰는걸 정의를 해줬으니 정의해준걸 쓰자.

		http.authorizeHttpRequests(security -> { // 여기서 HTTP 요청에 따른 권한별 설정. 자동으로 ROLE_를 넣어서 읽으니 테이블엔 ROLE_ADMIN 이런식으로 넣어
			//여기에 써준 해당 경로는 인증된 사용자만 접근 가능하도록 설정.
			security.requestMatchers("/posts/**").authenticated() //인증 통과만 하면 쓸수있음. 즉 로그인 한 사람만 이 URL 사용가능.
			.requestMatchers("/posts/update/**").hasRole("ADMIN")			
			.requestMatchers("/posts/delete/**").hasRole("ADMIN")
			.anyRequest().permitAll(); //위에 명시하지 않은, 인증도 안되고 롤도 없는 애들 전부. 이 URL 빼고 다 허용.
		});

		http.formLogin(frmLogin -> frmLogin.disable()); //폼 로그인 기반 비활성화
		http.sessionManagement(ssmg -> ssmg.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션 없애기. jwt 할거.
		http.addFilter(new JWTAuthenticationFilter(authConfig.getAuthenticationManager(), memberRepo)); //인증검사
		http.addFilter(new JWTAuthorizationFilter(authConfig.getAuthenticationManager(), memberRepo)); //권한검사
		//인증을 먼저 하고 그다음 권한을 봐야지 순서대로. 
		return http.build();
	}
}
