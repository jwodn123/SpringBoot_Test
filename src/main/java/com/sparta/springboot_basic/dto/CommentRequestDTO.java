package com.sparta.springboot_basic.dto;

import com.sparta.springboot_basic.entity.Timestamped;
import lombok.Getter;


@Getter
public class CommentRequestDTO extends Timestamped{

    private Long id;
    private String comm_content;

}
