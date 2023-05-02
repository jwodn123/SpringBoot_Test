package com.sparta.springboot_basic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //BAD_REQUEST
    USER_Duplicate(HttpStatus.BAD_REQUEST,"중복된 username 입니다.", 400),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다.", 400),
    WRITER_ONLY_MODIFY(HttpStatus.BAD_REQUEST,"작성자만 수정할 수 있습니다.", 400),
    WRITER_ONLY_DELETE(HttpStatus.BAD_REQUEST,"작성자만 삭제할 수 있습니다.", 400),

    //NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"회원을 찾을 수 없습니다.", 400),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"게시글을 찾을 수 없습니다.", 400),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다.", 400);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final int statusCode;

    ErrorCode(HttpStatus httpStatus, String errorMessage, int statusCode) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}
