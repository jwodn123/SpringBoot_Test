package com.sparta.springboot_basic.config;

import com.sparta.springboot_basic.jwt.JwtAuthFilter;
import com.sparta.springboot_basic.jwt.JwtUtil;
import com.sparta.springboot_basic.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig { //기본적인 시큐리티 설정 및 구현 클래스

    private final JwtUtil jwtUtil;

    @Bean //비밀번호 암호화 기능
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //CSRF 설정
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests() // HTTP 요청에 대한 권한 부여 규칙을 구성
                .requestMatchers("/api/user/**").permitAll() //모든 사용자가 /api/user/로 시작하는 모든 URL에 액세스할 수 있음
                .anyRequest().authenticated() //위 요청을 제외한 모든 요청은 인증이 필요함을 나타냄.
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //폼 로그인 인증방식
        http.formLogin();

        //httpBasic 로그인 인증 방식
        //http.httpBasic().and();

        return http.build();
    }

}
