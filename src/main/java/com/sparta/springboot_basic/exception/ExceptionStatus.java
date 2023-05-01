package com.sparta.springboot_basic.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionStatus {

    USER_NOT_FOUND("회원을 찾을 수 없습니다."),
    USER_Duplicate("중복된 username 입니다."),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),
    TOKEN_NOT_FOUND("토큰이 유효하지 않습니다."),
    WRITER_ONLY_MODIFY("작성자만 수정할 수 있습니다."),
    WRITER_ONLY_DELETE("작성자만 삭제할 수 있습니다.");

    private final String message;

    public String getStatus(){
        return name();
    }

    public String getMessage(){
        return message;
    }
}
