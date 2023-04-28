package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.CommentRequestDTO;
import com.sparta.springboot_basic.dto.CommentResponseDTO;
import com.sparta.springboot_basic.security.UserDetailsImpl;
import com.sparta.springboot_basic.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 작성 API
    @PostMapping("/comment/{board_id}")
    public ResponseEntity<CommentResponseDTO> commRegist(@PathVariable Long board_id, @RequestBody CommentRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commRegist(board_id, requestDTO, userDetails.getUser());
    }

    //댓글 수정 API
    @PutMapping("/comment/{board_id}")
    public ResponseEntity<CommentResponseDTO> commUpdate(@PathVariable Long board_id, @RequestBody CommentRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commUpdate(board_id, requestDTO, userDetails.getUser());
    }

    //댓글 삭제 AP
    @DeleteMapping("/comment/{board_id}")
    public ResponseEntity<String> commDelete(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.commDelete(board_id, userDetails.getUser());
    }

}
