package com.project.snsserver.domain.board.controller;

import com.project.snsserver.domain.board.model.dto.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.EditCommentResponse;
import com.project.snsserver.domain.board.service.CommentService;
import com.project.snsserver.domain.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
    public ResponseEntity<EditCommentResponse> writeComment(@PathVariable Long postId,
                                                            @RequestBody @Valid EditCommentRequest request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        EditCommentResponse response = commentService.writeComment(postId, request, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, String> response = commentService.deleteComment(postId, commentId, userDetails.getMember());
        return ResponseEntity.ok(response);
    }
}
