package com.sparta.springboot_basic.exception;

import lombok.Getter;

@Getter
public enum ExceptionStatus {

    USER_NOT_FOUND("회원을 찾을 수 없습니다.", 400),
    USER_Duplicate("중복된 username 입니다.", 400),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", 400),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", 400),
    TOKEN_NOT_FOUND("토큰이 유효하지 않습니다.", 400),
    WRITER_ONLY_MODIFY("작성자만 수정할 수 있습니다.", 400),
    WRITER_ONLY_DELETE("작성자만 삭제할 수 있습니다.", 400);

    private final String errorMessage;
    private final int statusCode;

    ExceptionStatus(String errorMessage, int statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}
