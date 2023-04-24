package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.CommentRequestDTO;
import com.sparta.springboot_basic.dto.CommentResponseDTO;
import com.sparta.springboot_basic.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 작성 API
    @PostMapping("/comment/{board_id}")
    public CommentResponseDTO commRegist(@PathVariable Long board_id, @RequestBody CommentRequestDTO requestDTO, HttpServletRequest request) {
        return commentService.commRegist(board_id, requestDTO, request);
    }

    //댓글 수정 API
    @PutMapping("/comment/{board_id}")
    public CommentResponseDTO commUpdate(@PathVariable Long board_id, @RequestBody CommentRequestDTO requestDTO, HttpServletRequest request) {
        return commentService.commUpdate(board_id, requestDTO, request);
    }

    //댓글 삭제 AP
    @DeleteMapping("/comment/{board_id}")
    public String commDelete(@PathVariable Long board_id, HttpServletRequest request) {
        return commentService.commDelete(board_id, request);
    }

}
