package com.sparta.springboot_basic.service;

import com.sparta.springboot_basic.dto.LikeRequestDTO;
import com.sparta.springboot_basic.entity.Board;
import com.sparta.springboot_basic.entity.Comment;
import com.sparta.springboot_basic.entity.Like;
import com.sparta.springboot_basic.entity.User;
import com.sparta.springboot_basic.repository.BoardRepository;
import com.sparta.springboot_basic.repository.CommentRepository;
import com.sparta.springboot_basic.repository.LikeRespository;
import com.sparta.springboot_basic.repository.UserRepository;
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
public class LikeService {

    private final LikeRespository likeRespository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //게시글 좋아요
    public ResponseEntity<String> boardLike(Long id, LikeRequestDTO likeRequestDTO, User user) {

        //게시물 id 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 없습니다.")
        );

        Like like = null;
        List<Like> likes = board.getLikes();
        for (Like l : likes) {
            if (l.getUser().getUsername().equals(user.getUsername())) {
                like = l;
                break;
            }
        }

        if (like != null && !likeRequestDTO.isGood()) {
            like.togglbLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 취소!");
        } else if (like == null && likeRequestDTO.isGood()) {
            like = new Like(likeRequestDTO, board, null, user);
            like.togglbLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 등록!");
        } else if (like != null && likeRequestDTO.isGood() && like.getBlikeNum() != 1) { //취소 후 다시 좋아요를 누를 시
            like.togglbLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 등록!");
        } else if (like == null && !likeRequestDTO.isGood()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요가 등록되지 않았습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 좋아요를 누르셨습니다.");
        }
    }


    //댓글 좋아요
    public ResponseEntity<String> commentLike(Long id, LikeRequestDTO likeRequestDTO, User user) {

        //댓글 id 확인
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.")
        );

        Like like = null;
        List<Like> likes = comment.getLikes();
        for (Like l : likes) {
            if (l.getUser().getUsername().equals(user.getUsername())) {
                like = l;
                break;
            }
        }

        if (like != null && !likeRequestDTO.isGood()) {
            like.togglecLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 취소!");
        } else if (like == null && likeRequestDTO.isGood()) {
            like = new Like(likeRequestDTO, null, comment, user);
            like.togglecLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 등록!");
        } else if (like != null && likeRequestDTO.isGood() && like.getClikeNum() != 1) { //취소 후 다시 좋아요를 누를 시
            like.togglecLike(likeRequestDTO);
            likeRespository.save(like);
            return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 등록!");
        } else if (like == null && !likeRequestDTO.isGood()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요가 등록되지 않았습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 좋아요를 누르셨습니다.");
        }
    }
}
