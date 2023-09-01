package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.board.model.dto.PostHeartResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostHeart;
import com.project.snsserver.domain.board.repository.jpa.PostHeartRepository;
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

import java.util.Optional;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostHeartServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostHeartRepository postHeartRepository;

    @InjectMocks
    private PostHeartServiceImpl postHeartService;

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
    @DisplayName("좋아요 등록 성공")
    void pushHeart_Success() {

        Member member1 = Member.builder()
                .id(2L)
                .email("test@test.com")
                .nickname("nick")
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postHeartRepository.existsByPostAndMember(any(), any())).willReturn(false);

        ArgumentCaptor<PostHeart> captor = ArgumentCaptor.forClass(PostHeart.class);

        postHeartService.pushHeart(1L,  member1);

        verify(postHeartRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("좋아요 등록 실패1")
    void pushHeart_Fail1() {

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.pushHeart(1L, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("좋아요 등록 실패2")
    void pushHeart_Fail2() {

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.pushHeart(1L, member));

        assertEquals(exception.getErrorCode(), FAIL_TO_PUSH_HEART);
        assertEquals(exception.getMessage(), FAIL_TO_PUSH_HEART.getMessage());
    }

    @Test
    @DisplayName("좋아요 등록 실패3")
    void pushHeart_Fail3() {

        Member member1 = Member.builder()
                .id(2L)
                .email("test@test.com")
                .nickname("nick")
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postHeartRepository.existsByPostAndMember(any(), any())).willReturn(true);

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.pushHeart(1L, member1));

        assertEquals(exception.getErrorCode(), ALREADY_PUSH_HEART);
        assertEquals(exception.getMessage(), ALREADY_PUSH_HEART.getMessage());
    }


    @Test
    @DisplayName("좋아요 취소 성공")
    void cancelHeart() {

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        PostHeart heart = PostHeart.builder()
                .post(post)
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postHeartRepository.findByPostAndMember(any(), any())).willReturn(Optional.of(heart));

        ArgumentCaptor<PostHeart> captor = ArgumentCaptor.forClass(PostHeart.class);

        postHeartService.cancelHeart(1L, 1L, member);

        verify(postHeartRepository, times(1)).delete(captor.capture());
    }

    @Test
    @DisplayName("좋아요 취소 실패1")
    void cancelHeart_Fail1() {

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.cancelHeart(1L, 1L, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("좋아요 취소 실패2")
    void cancelHeart_Fail2() {

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postHeartRepository.findByPostAndMember(any(), any())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.cancelHeart(1L, 1L, member));

        assertEquals(exception.getErrorCode(), POST_HEART_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_HEART_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("특정 게시물 좋아요 조회 성공")
    void getPostHeartCountByPost_Success() {

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postHeartRepository.countByPost(any())).willReturn(10L);

        PostHeartResponse response = postHeartService.getPostHeartCountByPost(1L);

        assertEquals(response.getPostId(), response.getPostId());
        assertEquals(response.getPostHeartCount(), response.getPostHeartCount());
    }

    @Test
    @DisplayName("특정 게시물 좋아요 조회 실패")
    void getPostHeartCountByPost_Fail() {

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postHeartService.getPostHeartCountByPost(1L));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }
}