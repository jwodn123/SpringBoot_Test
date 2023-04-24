package com.sparta.springboot_basic.entity;

import com.sparta.springboot_basic.dto.CommentRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comm_content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequestDTO requestDTO, Board board, User user) {
        this.comm_content = requestDTO.getComm_content();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDTO requestDTO, User user) {
        this.comm_content = requestDTO.getComm_content();
        this.user = user;
    }

}
