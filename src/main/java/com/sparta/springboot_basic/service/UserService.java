package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.LoginRequestDTO;
import com.sparta.springboot_basic.dto.SignRequestDTO;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.jwt.JwtUtil;
import com.sparta.springboot_basic.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String usersignup(SignRequestDTO signRequestDTO) {
        String username = signRequestDTO.getUsername();
        String password = signRequestDTO.getPassword();

        // 사용자 중복 체크
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);

        return "회원가입에 성공했습니다!";
    }

    @Transactional(readOnly = true)
    public String userlogin(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return "토근을 Header에 추가 성공!";
    }
}
