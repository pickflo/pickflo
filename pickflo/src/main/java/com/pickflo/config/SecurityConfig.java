package com.pickflo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.pickflo.repository.UserRepository;
import com.pickflo.service.UserService;

@Configuration
//-> 스프링 컨테이너에서 생성하고 관리하는 설정 컴포넌트.
//-> 스프링 컨테이너에서 필요한 곳에 의존성 주입을 해줌.
@EnableMethodSecurity
//-> 컨트롤러 메서드에서 인증(로그인), 권한 설정을 하기 위해서.
public class SecurityConfig {
  
  // Spring Security 5 버전부터 비밀번호는 반드시 암호화를 해야만 함.
  // 만약 비밀번호를 암호화하지 않으면, HTTP 403(access denied, 접근 거부) 또는
  // HTTP 500(internal server error, 내부 서버 오류) 에러가 발새함.
  // 비밀번호를 암호화하는 객체를 스프링 컨테이너가 bean으로 관리해야 함.
  @Bean
  PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }
  
  // 스프링 시큐리티 필터 체인 객체(bean)
  // 로그인/로그아웃, 인증 필터에서 필요한 설정을 구성.
  // - 로그인 페이지(뷰), 로그아웃 페이지 설정.
  // - 페이지 접근 권한(ADMIN, USER) 설정.
  // - 인증 설정(로그인 없이 접근 가능한 페이지 vs 로그인해야만 접근 가능한 페이지)
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // 시큐리티 관련 설정들을 구성.
      
      // CSRF(Cross Site Request Forgery) 기능을 비활성화:
      // CSRF 기능을 활성화한 경우에는,
      // Ajax POST/PUT/DELETE 요청에서 csrf 토큰을 서버로 전송하지 않으면 HTTP 403 에러가 발생함.
      http.csrf((csrf) -> csrf.disable());
      
      // 로그인 페이지(폼) 설정 - 스프링 시큐리티에서 제공하는 기본 HTML 페이지를 사용.
      // http.formLogin(Customizer.withDefaults());
      // Custom 로그인 HTML 페이지를 사용.     
      http
	      .authorizeHttpRequests((requests) -> requests
	          .requestMatchers("/user/signin", "/user/signup").permitAll()
	          .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 자원 접근 허용
	          .requestMatchers("/api/**").permitAll() // API 경로에 대한 접근 허용 
	          .anyRequest().authenticated()
	      )
	      .formLogin((form) -> form
	              .loginPage("/user/signin")
	              .usernameParameter("email")  // email 필드를 username으로 사용
	              .passwordParameter("password")
	              .defaultSuccessUrl("/home", true)
	              .permitAll()
	          )
	      .logout((logout) -> logout
	          .logoutUrl("/user/logout")
	          .logoutSuccessUrl("/user/signin")
	          .permitAll()
	      );

      return http.build(); // DefaultSecurityFilterChain 객체를 생성해서 리턴
  }  
}

