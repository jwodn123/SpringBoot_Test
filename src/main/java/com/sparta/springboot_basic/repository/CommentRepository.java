package com.sparta.springboot_basic.repository;

import com.sparta.springboot_basic.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
