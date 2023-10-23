package com.project.snsserver.domain.board.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.project.snsserver.domain.board.model.dto.response.CommentResponse;
import com.project.snsserver.domain.board.model.dto.request.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.response.EditCommentResponse;
import com.project.snsserver.domain.board.service.CommentService;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.util.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "댓글", description = "게시판 API Document - 댓글")
public class CommentController {

	private final CommentService commentService;

	/**
	 * 댓글 작성
	 */
	@Operation(summary = "댓글 작성")
	@PostMapping("/{postId}/comments")
	public ResponseEntity<EditCommentResponse> writeComment(@PathVariable Long postId, @AuthMember Member member,
		@RequestBody @Valid EditCommentRequest request) {

		EditCommentResponse response = commentService.writeComment(postId, request, member);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 댓글 삭제
	 */
	@Operation(summary = "댓글 삭제")
	@DeleteMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
		@AuthMember Member member) {

		Map<String, String> response = commentService.deleteComment(postId, commentId, member);
		return ResponseEntity.ok(response);
	}

	/**
	 * 댓글 수정
	 */
	@Operation(summary = "댓글 수정")
	@PatchMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<EditCommentResponse> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@RequestBody @Valid EditCommentRequest request, @AuthMember Member member) {

		EditCommentResponse response = commentService.updateComment(postId, commentId, request, member);
		return ResponseEntity.ok(response);
	}

	/**
	 * 게시물 댓글 조회
	 */
	@Operation(summary = "게시물 댓글 조회")
	@GetMapping("/{postId}/comments")
	public ResponseEntity<Slice<CommentResponse>> getCommentsByPost(@PathVariable Long postId,
		@RequestParam(required = false) Long lastCommentId, @AuthMember Member member,
		@PageableDefault Pageable pageable) {

		Slice<CommentResponse> response
			= commentService.getCommentsByPost(postId, lastCommentId, member.getEmail(), pageable);
		return ResponseEntity.ok(response);
	}
}
