package com.sparta.springboot_basic.repository;

import com.sparta.springboot_basic.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRespository extends JpaRepository<Like, Long> {
}
