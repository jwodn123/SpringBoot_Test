package com.sparta.springboot_basic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignRequestDTO { //회원가입 시 데이터를 받아온다

    private String username;
    private String password;

}
