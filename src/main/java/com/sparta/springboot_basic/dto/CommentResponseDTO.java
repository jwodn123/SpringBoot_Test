package com.sparta.springboot_basic.dto;

import com.sparta.springboot_basic.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String comm_content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String username;


    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.comm_content = comment.getComm_content();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.username = comment.getUser().getUsername();
    }
}
