package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.dto.CommentRequestDTO;
import com.sparta.springboot_basic.dto.CommentResponseDTO;
import com.sparta.springboot_basic.entity.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    public ResponseEntity<CommentResponseDTO> commRegist(Long board_id, CommentRequestDTO requestDTO, User user) {

        // 선택한 게시글에 대한 DB 정보 유무 확인
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다.")
        );

        List<Like> likes = new ArrayList<>();
        Comment comment = commentRepository.save(new Comment(requestDTO, board, user, likes));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDTO(comment));
    }

    //댓글 수정
    public ResponseEntity<CommentResponseDTO> commUpdate(Long board_id, CommentRequestDTO requestDTO, User user) {

        // 선택한 게시글의 댓글 유무 확인
        Comment comment = commentRepository.findById(board_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다.")
        );

        //사용자 본인이거나, 권한이 ADMIN이 아니면 수정 불가
        if(!user.getUsername().equals(comment.getUser().getUsername()) && user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정할 수 있습니다");
        }

        comment.update(requestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDTO(comment));
    }

    //댓글 삭제
    public ResponseEntity<String> commDelete(Long board_id, User user) {

        // 선택한 게시글의 댓글 유무 확인
        Comment comment = commentRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        //사용자 본인이거나, 권한이 ADMIN이 아니면 삭제 불가
        if(!user.getUsername().equals(comment.getUser().getUsername()) && user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭할 수 있습니다");
        }

        commentRepository.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공!");
    }

}
