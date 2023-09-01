package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}