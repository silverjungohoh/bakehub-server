package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
