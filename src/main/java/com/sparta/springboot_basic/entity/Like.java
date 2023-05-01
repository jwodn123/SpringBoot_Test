package com.sparta.springboot_basic.entity;

import com.sparta.springboot_basic.dto.LikeRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "likes")
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;
    private int blikeNum = 0;
    private int clikeNum = 0;
    private boolean good;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Like(LikeRequestDTO likeRequestDTO, Board board, Comment comment, User user) {
        this.good = likeRequestDTO.isGood();
        this.board = board;
        this.comment = comment;
        this.user = user;
    }

    public void togglbLike(LikeRequestDTO likeRequestDTO) {
        if (likeRequestDTO.isGood()) {
            blikeNum++;
        } else if(!likeRequestDTO.isGood()) {
            blikeNum--;
        }
    }

    public void togglecLike(LikeRequestDTO likeRequestDTO) {
        if (likeRequestDTO.isGood()) {
            clikeNum++;
        } else if(!likeRequestDTO.isGood()) {
            clikeNum--;
        }
    }
}
