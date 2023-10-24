package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostImage;
import com.project.snsserver.domain.board.repository.jpa.custom.CustomPostImageRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, CustomPostImageRepository {

	List<PostImage> findAllByPost(Post post);

	Optional<PostImage> findByIdAndPost(Long postImageId, Post post);
}
