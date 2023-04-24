package com.sparta.springboot_basic.entity;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc") //댓글은 작성 날짜 기준 내림차순으로 정렬
    private List<Comment> comments;


    public Board(BoardRequestDTO requestDTO, User user, List<Comment> comments) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.user = user;
        this.comments = comments;
    }

    public void update(BoardRequestDTO requestDTO, User user, List<Comment> comments) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.user = user;
        this.comments = comments;
    }

}
