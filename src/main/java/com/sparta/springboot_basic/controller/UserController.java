package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.LoginRequestDTO;
import com.sparta.springboot_basic.dto.SignRequestDTO;
import com.sparta.springboot_basic.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
