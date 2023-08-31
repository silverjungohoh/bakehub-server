package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.project.snsserver.domain.member.model.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {

    /**
     * 글 작성
     */
    EditPostResponse writePost(EditPostRequest request, List<MultipartFile> files, Member member);

    /**
     * 글 수정
     */
    EditPostResponse updatePost(Long postId, EditPostRequest request, Member member);

    /**
     * 글 삭제
     */
    Map<String, String> deletePost(Long postId, Member member);

    /**
     * 게시물 수정 시 이미지 등록
     */
    PostImageResponse addPostImage(Long postId, MultipartFile file);
}
