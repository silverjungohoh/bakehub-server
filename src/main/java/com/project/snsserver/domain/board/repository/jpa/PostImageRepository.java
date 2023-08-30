package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
