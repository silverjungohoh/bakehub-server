package com.project.snsserver.domain.board.repository.jpa;

import com.project.snsserver.domain.board.model.dto.PostDetailResponse;
import com.project.snsserver.domain.board.model.dto.PostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomPostRepository {


    Slice<PostResponse> findAllPostsWithCommentCntAndHeartCnt(Long lastPostId, Pageable pageable);

    PostDetailResponse findPostDetailByPostId(Long postId);

    Slice<PostResponse> findAllPostsByHashtag(Long lastPostId, String name, Pageable pageable);
}
