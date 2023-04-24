package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import com.sparta.springboot_basic.dto.BoardResponseDTO;
import com.sparta.springboot_basic.dto.CommentResponseDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    // 전체 게시글 조회
    public List<BoardResponseDTO> getboardList() {
        return boardRepository.findAllByOrderByModifiedAtDesc()
                              .stream()
                              .map(BoardResponseDTO::new)
                              .collect(Collectors.toList());
    }

    // 단일 게시글 조회
    public BoardResponseDTO getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("선택한 게시물이 없습니다!")
        );

        return new BoardResponseDTO(board);
    }

    // 게시글 생성
    public BoardResponseDTO createBoard(BoardRequestDTO requestDTO, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 등록
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

            List<Comment> comments = new ArrayList<>();

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Board board = boardRepository.saveAndFlush(new Board(requestDTO, user, comments));
            return new BoardResponseDTO(board);
        } else {
            return null;
        }
    }

    //게시글 수정
    public BoardResponseDTO updateBoard(Long id, BoardRequestDTO requestDTO, HttpServletRequest request) {

        //게시물 id 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정할 게시물이 없습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 수정
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            //사용자 권한 ADMIN 일 때
            User user = new User();
            UserRole role = user.getRole();
            if(role == UserRole.ADMIN) {
                List<Comment> comments = new ArrayList<>();
                board.update(requestDTO, user, comments);
                return new BoardResponseDTO(board);
            } else {
                // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
                user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정할 수 있습니다")
                );

                List<Comment> comments = new ArrayList<>();
                board.update(requestDTO, user, comments);
                return new BoardResponseDTO(board);
            }
        } else {
            return null;
        }
    }

    //게시글 삭제
    public String deleteBoard(Long id, HttpServletRequest request) {

        //게시물 id 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("삭제할 게시물이 없습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 삭제
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
            }

            //사용자 권한 ADMIN 일 때
            User user = new User();
            UserRole role = user.getRole();
            if(role == UserRole.ADMIN) {
                boardRepository.delete(board);
            } else {
                // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
                user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제할 수 있습니다")
                );

                boardRepository.delete(board);
            }
            return "게시물 삭제 성공!";
        } else {
            return "게시물 삭제 실패!";
        }

    }


//    //토큰 확인 및 검증하는 함수
//    public void checkToken(HttpServletRequest request) {
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 게시글 수정
//        if (jwtUtil.validateToken(token)) {
//            // 토큰에서 사용자 정보 가져오기
//            claims = jwtUtil.getUserInfoFromToken(token);
//        } else {
//            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
//        }
//
//        // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//        );
//    }
}
