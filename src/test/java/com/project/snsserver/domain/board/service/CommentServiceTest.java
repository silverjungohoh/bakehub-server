package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.EditCommentRequest;
import com.project.snsserver.domain.board.model.dto.EditCommentResponse;
import com.project.snsserver.domain.board.model.entity.Comment;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.repository.jpa.CommentRepository;
import com.project.snsserver.domain.board.repository.jpa.PostRepository;
import com.project.snsserver.domain.member.model.entity.Member;
import com.project.snsserver.global.error.exception.BoardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .email("email@gmail.com")
                .nickname("닉네임")
                .build();
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void writeComment_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        EditCommentRequest request = EditCommentRequest.builder()
                .content("댓글")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);

        EditCommentResponse response = commentService.writeComment(1L, request, member);

        assertEquals(response.getContent(), request.getContent());
        assertEquals(response.getNickname(), member.getNickname());
        verify(commentRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("댓글 작성 실패")
    void writeComment_Fail() {

        EditCommentRequest request = EditCommentRequest.builder()
                .content("댓글")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> commentService.writeComment(1L, request, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .build();

        given(postRepository.existsById(anyLong())).willReturn(true);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);

        Map<String, String> response = commentService.deleteComment(1L, 1L, member);

        assertNotNull(response.get("result"));
        verify(commentRepository, times(1)).delete(captor.capture());
    }

    @Test
    @DisplayName("댓글 삭제 실패1")
    void deleteComment_Fail1() {

        given(postRepository.existsById(anyLong())).willReturn(false);

        BoardException exception = assertThrows(BoardException.class,
                () -> commentService.deleteComment(1L, 1L, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 실패2")
    void deleteComment_Fail2() {

        Member member1 = Member.builder()
                .id(2L)
                .email("test@test.com")
                .nickname("nick")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .build();

        given(postRepository.existsById(anyLong())).willReturn(true);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        BoardException exception = assertThrows(BoardException.class,
                () -> commentService.deleteComment(1L, 1L, member1));

        assertEquals(exception.getErrorCode(), FAIL_TO_DELETE_COMMENT);
        assertEquals(exception.getMessage(), FAIL_TO_DELETE_COMMENT.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .build();

        EditCommentRequest request = EditCommentRequest.builder()
                .content("수정 댓글")
                .build();

        given(postRepository.existsById(anyLong())).willReturn(true);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        EditCommentResponse response = commentService.updateComment(1L, 1L, request, member);
        assertEquals(response.getContent(), request.getContent());
        assertEquals(response.getNickname(), member.getNickname());
    }

    @Test
    @DisplayName("댓글 수정 실패1")
    void updateComment_Fail1() {

        EditCommentRequest request = EditCommentRequest.builder()
                .content("수정 댓글")
                .build();

        given(postRepository.existsById(anyLong())).willReturn(false);

        BoardException exception = assertThrows(BoardException.class,
                () -> commentService.updateComment(1L, 1L, request, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 실패2")
    void updateComment_Fail2() {

        Member member1 = Member.builder()
                .id(2L)
                .email("test@test.com")
                .nickname("nick")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .build();

        EditCommentRequest request = EditCommentRequest.builder()
                .content("수정 댓글")
                .build();

        given(postRepository.existsById(anyLong())).willReturn(true);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        BoardException exception = assertThrows(BoardException.class,
                () -> commentService.updateComment(1L, 1L, request, member1));

        assertEquals(exception.getErrorCode(), FAIL_TO_UPDATE_COMMENT);
        assertEquals(exception.getMessage(), FAIL_TO_UPDATE_COMMENT.getMessage());
    }
}