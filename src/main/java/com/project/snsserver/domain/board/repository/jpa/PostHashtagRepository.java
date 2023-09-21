package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.PostHashtag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long>, CustomPostHashtagRepository {
}
