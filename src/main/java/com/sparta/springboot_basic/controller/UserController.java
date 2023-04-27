package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.LoginRequestDTO;
import com.sparta.springboot_basic.dto.SignRequestDTO;
import com.sparta.springboot_basic.entity.UserRole;
import com.sparta.springboot_basic.repository.UserRepository;
import com.sparta.springboot_basic.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입 API
    @PostMapping("/signup")
        public String usersignup(@RequestBody @Valid SignRequestDTO signRequestDTO) {
        return userService.usersignup(signRequestDTO);
    }

    // 로그인 API
    @ResponseBody //http요청 body를 자바 객체로 전달받을 수 있다.
    @PostMapping("/login")
    public String userlogin(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        return userService.userlogin(loginRequestDTO, response);
    }


}
