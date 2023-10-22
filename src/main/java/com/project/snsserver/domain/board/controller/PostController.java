package com.project.snsserver.domain.board.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.PostDetailResponse;
import com.project.snsserver.domain.board.model.dto.PostHashtagResponse;
import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.project.snsserver.domain.board.model.dto.PostResponse;
import com.project.snsserver.domain.board.service.PostService;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "게시물", description = "게시판 API Document - 게시물")
public class PostController {

	private final PostService postService;

	/**
	 * 글 작성
	 */
	@Operation(summary = "글 작성")
	@PostMapping
	public ResponseEntity<EditPostResponse> writePost(
		@RequestPart(value = "images", required = false) List<MultipartFile> files,
		@RequestPart(value = "data") @Valid EditPostRequest request, @AuthMember Member member) {

		EditPostResponse response = postService.writePost(request, files, member);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 글 수정
	 */
	@Operation(summary = "글 수정")
	@PatchMapping("/{postId}")
	public ResponseEntity<EditPostResponse> updatePost(@PathVariable Long postId,
		@RequestBody @Valid EditPostRequest request, @AuthMember Member member) {

		EditPostResponse response = postService.updatePost(postId, request, member);
		return ResponseEntity.ok(response);
	}

	/**
	 * 글 삭제
	 */
	@Operation(summary = "글 삭제")
	@DeleteMapping("/{postId}")
	public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long postId, @AuthMember Member member) {

		Map<String, String> response = postService.deletePost(postId, member);
		return ResponseEntity.ok(response);
	}

	/**
	 * 이미지 등록
	 */
	@Operation(summary = "이미지 등록")
	@PostMapping("/{postId}/images")
	public ResponseEntity<PostImageResponse> addPostImage(@PathVariable Long postId,
		@RequestPart(value = "image") MultipartFile file) {

		PostImageResponse response = postService.addPostImage(postId, file);
		return ResponseEntity.ok(response);
	}

	/**
	 * 이미지 삭제
	 */
	@Operation(summary = "이미지 삭제")
	@DeleteMapping("/{postId}/images/{postImageId}")
	public ResponseEntity<Map<String, String>> deletePostImage(@PathVariable Long postId,
		@PathVariable Long postImageId) {

		Map<String, String> response = postService.deletePostImage(postId, postImageId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 글 목록 조회
	 */
	@Operation(summary = "글 목록 조회")
	@GetMapping
	public ResponseEntity<Slice<PostResponse>> getPosts(@PageableDefault Pageable pageable,
		@RequestParam(required = false) Long lastPostId) {

		Slice<PostResponse> response = postService.getPosts(lastPostId, pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * 글 상세 조회
	 */
	@Operation(summary = "글 상세 조회")
	@GetMapping("/{postId}")
	public ResponseEntity<PostDetailResponse> getPostDetail(@AuthMember Member member, @PathVariable Long postId) {

		PostDetailResponse response = postService.getPostDetail(postId, member.getId());
		return ResponseEntity.ok(response);
	}

	/**
	 * 글 해시태그 목록 조회
	 */
	@Operation(summary = "글 해시태그 목록 조회")
	@GetMapping("/{postId}/tags")
	public ResponseEntity<List<PostHashtagResponse>> getPostHashtagsByPost(@PathVariable Long postId) {

		List<PostHashtagResponse> response = postService.getPostHashtags(postId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 해시태그에 따른 글 목록 조회
	 */
	@Operation(summary = "해시태그에 따른 글 목록 조회")
	@GetMapping("/search")
	public ResponseEntity<Slice<PostResponse>> getPostsByHashtag(@RequestParam(required = false) Long lastPostId,
		@RequestParam String tag, @PageableDefault Pageable pageable) {

		Slice<PostResponse> response = postService.getPostsByHashtag(lastPostId, tag, pageable);
		return ResponseEntity.ok(response);
	}
}
