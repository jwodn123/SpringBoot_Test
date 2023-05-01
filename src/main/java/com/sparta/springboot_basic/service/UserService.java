package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.LoginRequestDTO;
import com.sparta.springboot_basic.dto.SignRequestDTO;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.entity.UserRole;
import com.sparta.springboot_basic.jwt.JwtUtil;
import com.sparta.springboot_basic.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.secret.key}")
    private String ADMIN_TOKEN;

    @Transactional
    public ResponseEntity<String> usersignup(SignRequestDTO signRequestDTO) {
        String username = signRequestDTO.getUsername();
        String password = passwordEncoder.encode(signRequestDTO.getPassword()); //비밀번호 암호화

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (signRequestDTO.isAdmin()) {
            if (!signRequestDTO.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공!");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> userlogin(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공!");
    }
}
