package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostHeart;
import com.project.snsserver.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {

    boolean existsByPostAndMember(Post post, Member member);

    Optional<PostHeart> findByPostAndMember(Post post, Member member);
}
