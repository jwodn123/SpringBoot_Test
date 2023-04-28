package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.entity.Board;
import com.sparta.springboot_basic.entity.Comment;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.entity.UserRole;
import com.sparta.springboot_basic.jwt.JwtUtil;
import com.sparta.springboot_basic.repository.BoardRepository;
import com.sparta.springboot_basic.repository.CommentRepository;
import com.sparta.springboot_basic.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 전체 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, List<BoardResponseDTO>>> getboardList() {
        //return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDTO::new).collect(Collectors.toList());
        List<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDTO> boardList = new ArrayList<>();
        for(Board board : boards) {
            boardList.add(new BoardResponseDTO(board));
        }

        Map<String, List<BoardResponseDTO>> result = new HashMap<>();
        result.put("postList", boardList);
        return ResponseEntity.ok().body(result);
    }

    // 단일 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<BoardResponseDTO> getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("선택한 게시물이 없습니다!")
        );

        return ResponseEntity.status(HttpStatus.OK).body(new BoardResponseDTO(board));

    }

    // 게시글 생성
    @Transactional
    public ResponseEntity<BoardResponseDTO> createBoard(BoardRequestDTO requestDTO, User user) {

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        List<Comment> comments = new ArrayList<>();
        Board board = boardRepository.saveAndFlush(new Board(requestDTO, user, comments));
        return ResponseEntity.status(HttpStatus.CREATED).body(new BoardResponseDTO(board));

    }

    //게시글 수정
    @Transactional
    public ResponseEntity<BoardResponseDTO> updateBoard(Long id, BoardRequestDTO requestDTO, User user) {

        //게시물 id 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 게시물이 없습니다.")
        );

        if(!user.getUsername().equals(board.getUser().getUsername()) && user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정할 수 있습니다");
        }

        List<Comment> comments = new ArrayList<>();
        board.update(requestDTO, user, comments);
        return ResponseEntity.status(HttpStatus.OK).body(new BoardResponseDTO(board));
    }

    //게시글 삭제
    @Transactional
    public ResponseEntity<String> deleteBoard(Long id, User user) {

        //게시물 id 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 게시물이 없습니다.")
        );

        if(!user.getUsername().equals(board.getUser().getUsername()) && user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제할 수 있습니다");
        }

        boardRepository.delete(board);
        return ResponseEntity.status(HttpStatus.OK).body("게시물 삭제 성공!");
    }
}
