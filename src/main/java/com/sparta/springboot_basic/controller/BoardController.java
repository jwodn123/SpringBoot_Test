package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.security.UserDetailsImpl;
import com.sparta.springboot_basic.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 전체 조회 API
    @GetMapping("/board")
    public ResponseEntity<Map<String, List<BoardResponseDTO>>> getboardList() {
        return boardService.getboardList();
    }

    //게시글 단일 조회 API
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDTO> getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    //게시글 생성 API
    @PostMapping("/board")
    public ResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDTO, userDetails.getUser());
    }

    //게시글 수정 API
    @PutMapping("/board/{id}")
    public ResponseEntity<BoardResponseDTO> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDTO, userDetails.getUser());
    }

    //게시글 삭제 API
    @DeleteMapping("/board/{id}")
    public ResponseEntity<String>  deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails.getUser());
    }


}
