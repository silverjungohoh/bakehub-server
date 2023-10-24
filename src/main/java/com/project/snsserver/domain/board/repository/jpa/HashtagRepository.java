package com.project.snsserver.domain.board.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.snsserver.domain.board.model.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	List<Hashtag> findAllByNameIn(List<String> tagNames);
}
