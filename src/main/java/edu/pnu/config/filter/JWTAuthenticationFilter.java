package edu.pnu.config.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
//Lombok 어노테이션으로 final 또는 @NonNull 필드만을 사용하여 생성자를 자동으로 생성. 
//여기서는 AuthenticationManager와 MemberRepository를 주입받기 위해 사용됩니다.
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager; // 스프링 시큐리티의 인증 메커니즘
	private final MemberRepository memRepo;

	@Override //로그인 요청 처리, 사용자 정보 추출하고 해당 정보로 인증시도
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper om = new ObjectMapper();
		try {

			Member member = om.readValue(request.getInputStream(), Member.class);
			// ------ 유저가 틀렸을때. 추가코드---------
			Optional<Member> option = memRepo.findById(member.getUsername());
			if (!option.isPresent()) {

				log.info("Not Authenticated : Not found user!");
				return null;
			}

			Authentication authToken = new UsernamePasswordAuthenticationToken(member.getUsername(),
					member.getPassword());
			Authentication auth = authenticationManager.authenticate(authToken);
			log.info("Authenticated:[" + member.getUsername() + "]");
			return auth;
		} catch (Exception e) {
			log.info("Not Authenticated : Not Match password!");
		}
		return null;
	}

	@Override //인증 성공시 호출되는 메서드. 성공후 JWT 토큰생성하고 응답헤더에 추가.
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		log.info("successfulAuthentication:" + user.toString());
		


		String jwtToken = JWT.create().withClaim("username", user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.sign(Algorithm.HMAC256("edu.pnu.jwtkey"));
		
		log.info("Generated JWT Token: " + jwtToken);

		response.addHeader("Authorization", "Bearer " + jwtToken);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"message\":\"로그인 성공\"}");
		// ,\"token\":\"" + jwtToken + "\"
		response.getWriter().flush(); // 출력 스트림 플러시
		response.getWriter().close(); // 출력 스트림 종료
//		chain.doFilter(request, response);

	}
}
