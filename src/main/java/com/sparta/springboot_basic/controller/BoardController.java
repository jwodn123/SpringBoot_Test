package com.sparta.springboot_basic.controller;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public BoardResponseDTO getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    //게시글 생성 API
    @PostMapping("/board")
    public BoardResponseDTO createBoard(@RequestBody BoardRequestDTO requestDTO, HttpServletRequest request) {
        return boardService.createBoard(requestDTO, request);
    }

    //게시글 수정 API
    @PutMapping("/board/{id}")
    public BoardResponseDTO updateBoard(@PathVariable Long id, @RequestBody BoardRequestDTO requestDTO, HttpServletRequest request) {
        return boardService.updateBoard(id, requestDTO, request);
    }

    //게시글 삭제 API
    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }


}
