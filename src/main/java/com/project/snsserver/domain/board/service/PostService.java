package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.*;
import com.project.snsserver.domain.member.model.entity.Member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

	/**
	 * 게시물 수정 시 이미지 삭제
	 */
	Map<String, String> deletePostImage(Long postId, Long postImageId);

	/**
	 * 게시물 목록 조회
	 */
	Slice<PostResponse> getPosts(Long lastPostId, Pageable pageable);

	/**
	 * 게시물 상세 조회
	 */
	PostDetailResponse getPostDetail(Long postId, Long memberId);

	/**
	 * 게시물의 해시태그 목록 조회
	 */
	List<PostHashtagResponse> getPostHashtags(Long postId);

	/**
	 * 해시태그에 따른 글 목록 조회
	 */
	Slice<PostResponse> getPostsByHashtag(Long lastPostId, String tag, Pageable pageable);
}