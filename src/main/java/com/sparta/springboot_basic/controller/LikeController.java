package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.LikeRequestDTO;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.security.UserDetailsImpl;
import com.sparta.springboot_basic.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    //게시글 좋아요
    @PostMapping("/blike/{id}")
    public ResponseEntity<String> boardLike(@PathVariable Long id, @RequestBody LikeRequestDTO likeRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.boardLike(id, likeRequestDTO, userDetails.getUser());
    }

    //댓글 좋아요
    @PostMapping("/clike/{id}")
    public ResponseEntity<String> commentLike(@PathVariable Long id, @RequestBody LikeRequestDTO likeRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.commentLike(id, likeRequestDTO, userDetails.getUser());
    }

}
