package com.sparta.springboot_basic.repository;

import com.sparta.springboot_basic.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc(); //시간을 기준으로 내림차순
}
