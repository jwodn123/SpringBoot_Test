package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.dto.CommentRequestDTO;
import com.sparta.springboot_basic.dto.CommentResponseDTO;
import com.sparta.springboot_basic.entity.Board;
import com.sparta.springboot_basic.entity.Comment;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.jwt.JwtUtil;
import com.sparta.springboot_basic.repository.BoardRepository;
import com.sparta.springboot_basic.repository.CommentRepository;
import com.sparta.springboot_basic.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    //댓글 작성
    public CommentResponseDTO commRegist(Long board_id, CommentRequestDTO requestDTO, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 등록
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 선택한 게시글에 대한 DB 정보 유무 확인
            Board board = boardRepository.findById(board_id).orElseThrow(
                    () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
            );

            // 댓글 저장
            Comment comment = commentRepository.save(new Comment(requestDTO, board, user));
            return new CommentResponseDTO(comment);
        } else {
            return null;
        }
    }

    //댓글 수정
    public CommentResponseDTO commUpdate(Long board_id, CommentRequestDTO requestDTO, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 수정
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정할 수 있습니다.")
            );

            // 선택한 게시글의 댓글 유무 확인
            Comment comment = commentRepository.findById(board_id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            // 댓글 수정
            comment.update(requestDTO, user);
            return new CommentResponseDTO(comment);
        } else {
            return null;
        }
    }

    //댓글 삭제
    public String commDelete(Long board_id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 댓글 삭제
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제할 수 있습니다.")
            );

            // 선택한 게시글의 댓글 유무 확인
            Comment comment = commentRepository.findById(board_id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            // 댓글 삭제
            commentRepository.delete(comment);
            return "댓글 삭제 성공";
        } else {
            return "댓글 삭제 실패";
        }
    }

}
