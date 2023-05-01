package com.sparta.springboot_basic.dto;

import com.sparta.springboot_basic.entity.Board;
import com.sparta.springboot_basic.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {

    private Long id;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDTO> comments;
    private int likeNum;

    public BoardResponseDTO(Board board) {
        this.id = board.getId();
        this.username = board.getUser().getUsername();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.comments = board.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList());
        this.likeNum = board.getLikes().stream().mapToInt(Like::getBlikeNum).sum();
    }

}

