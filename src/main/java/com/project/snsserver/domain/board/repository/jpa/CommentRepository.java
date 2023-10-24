package com.project.snsserver.domain.board.repository.jpa;

import java.util.Optional;

import com.project.snsserver.domain.board.model.entity.Comment;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.repository.jpa.custom.CustomCommentRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

	Optional<Comment> findByIdAndPost(Long commentId, Post post);
}
