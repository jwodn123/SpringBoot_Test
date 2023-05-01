package com.sparta.springboot_basic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponseDTO {

    private String status;
    private String message;
}
