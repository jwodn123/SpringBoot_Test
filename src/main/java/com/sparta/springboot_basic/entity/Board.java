package com.sparta.springboot_basic.entity;

import com.sparta.springboot_basic.dto.BoardRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String username;


    public Board(BoardRequestDTO requestDTO, String username) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.username = username;
    }

    public void update(BoardRequestDTO requestDTO, String username) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
        this.username = username;
    }

//    public Board(User user) {
//        this.user = user;
//    }


}
