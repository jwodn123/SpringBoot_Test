package com.sparta.springboot_basic.jwt;

import com.sparta.springboot_basic.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // Header KEY 값.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    private static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private static final long TOKEN_TIME = 60 * 60 * 1000L; //1h (60 * 1000L -> 1m)

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //JWT 서명을 생성하기 위해 널리 사용되는 HMAC-SHA256 알고리즘 중 하나인 HS256 알고리즘으로 초기화

    @PostConstruct // 종속성 주입이 완료된 후 실행해야 하는 메서드를 표시하는 데 사용되는 또 다른 Spring 애노테이션
    public void init() { //@PostConstruct 주석으로 표시된 초기화 방법
        byte[] bytes = Base64.getDecoder().decode(secretKey); //Base64 형식의 secretKey 값을 바이트 배열로 디코딩합니다. Base64는 ASCII 문자열 형식으로 이진 데이터를 나타내는 이진-텍스트 인코딩 체계이며 HTTP와 같은 텍스트 기반 채널을 통해 이진 데이터를 전송하는 데 자주 사용
        key = Keys.hmacShaKeyFor(bytes); //javax.crypto 인스턴스를 생성합니다.secretKey 값에서 디코딩된 바이트 배열을 사용하는 SecretKey. 키 필드에 할당합니다. Keys.hmacShaKeyFor()는 io.jsonwebtoken.security에서 제공하는 편리한 방법입니다.HMAC-SHA 서명 알고리즘에 사용할 바이트 배열에서 개인 키를 생성하는 키 클래스입니다.
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}