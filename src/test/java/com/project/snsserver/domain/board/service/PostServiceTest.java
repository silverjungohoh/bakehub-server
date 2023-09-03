package com.project.snsserver.domain.board.service;

import com.project.snsserver.domain.awss3.service.AwsS3Service;
import com.project.snsserver.domain.board.model.dto.EditPostRequest;
import com.project.snsserver.domain.board.model.dto.EditPostResponse;
import com.project.snsserver.domain.board.model.dto.PostImageResponse;
import com.project.snsserver.domain.board.model.entity.Post;
import com.project.snsserver.domain.board.model.entity.PostImage;
import com.project.snsserver.domain.board.repository.jpa.CommentRepository;
import com.project.snsserver.domain.board.repository.jpa.PostHeartRepository;
import com.project.snsserver.domain.board.repository.jpa.PostImageRepository;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.project.snsserver.global.error.type.BoardErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostHeartRepository postHeartRepository;

    @Mock
    private AwsS3Service awsS3Service;

    @InjectMocks
    private PostServiceImpl postService;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .id(1L)
                .email("email@gmail.com")
                .nickname("닉네임")
                .build();
    }

    @Test
    @DisplayName("글 작성 성공")
    void writePost_Success() {

        EditPostRequest request = EditPostRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        MockMultipartFile file = new MockMultipartFile("test", "test_2023.png", "image/png", "test".getBytes());

        String imgUrl = "https://image-bucket.s3.abc.jpg";
        given(awsS3Service.uploadFiles(anyList(), anyString())).willReturn(List.of(imgUrl));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        EditPostResponse response = postService.writePost(request, List.of(file), member);

        assertEquals(response.getTitle(), request.getTitle());
        assertEquals(response.getContent(), request.getContent());
        assertEquals(response.getNickname(), member.getNickname());
        assertEquals(1, response.getPostImages().size());
        verify(postRepository, times(1)).save(postCaptor.capture());
    }


    @Test
    @DisplayName("글 작성 실패")
    void writePost_Fail() {

        EditPostRequest request = EditPostRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        MockMultipartFile file1 = new MockMultipartFile("test1", "test_2023_1.png", "image/png", "test1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test_2023_2.png", "image/png", "test2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test_2023_3.png", "image/png", "test3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("test4", "test_2023_4.png", "image/png", "test4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("test5", "test_2023_5.png", "image/png", "test5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("test6", "test_2023_6.png", "image/png", "test6".getBytes());

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.writePost(request, List.of(file1, file2, file3, file4, file5, file6), member));

        assertEquals(exception.getErrorCode(), IMAGE_COUNT_EXCEEDED);
        assertEquals(exception.getMessage(), IMAGE_COUNT_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("글 수정 성공")
    void updatePost_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        EditPostRequest request = EditPostRequest.builder()
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        EditPostResponse response = postService.updatePost(1L, request, member);

        assertEquals(response.getTitle(), request.getTitle());
        assertEquals(response.getContent(), request.getContent());
        assertEquals(response.getNickname(), member.getNickname());
        assertEquals(0, response.getPostImages().size());
    }

    @Test
    @DisplayName("글 수정 실패1")
    void updatePost_Fail1() {

        EditPostRequest request = EditPostRequest.builder()
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.updatePost(1L, request, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("글 수정 실패2")
    void updatePost_Fail2() {

        Member member1 = Member.builder()
                .email("test1@test.com")
                .nickname("nickname1")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        EditPostRequest request = EditPostRequest.builder()
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.updatePost(1L, request, member1));

        assertEquals(exception.getErrorCode(), FAIL_TO_UPDATE_POST);
        assertEquals(exception.getMessage(), FAIL_TO_UPDATE_POST.getMessage());
    }

    @Test
    @DisplayName("글 삭제 성공")
    void deletePost_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(commentRepository.deleteCommentAllByPostId(anyLong())).willReturn(1L);
        given(postHeartRepository.deletePostHeartAllByPostId(anyLong())).willReturn(1L);
        given(postImageRepository.deleteAllPostImageByPostId(anyLong())).willReturn(1L);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        Map<String, String> response = postService.deletePost(1L, member);

        assertNotNull(response.get("result"));
        verify(postRepository, times(1)).delete(postCaptor.capture());
    }

    @Test
    @DisplayName("글 삭제 실패1")
    void deletePost_Fail1() {

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.deletePost(1L, member));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("글 삭제 실패2")
    void deletePost_Fail2() {

        Member member1 = Member.builder()
                .email("test1@test.com")
                .nickname("nickname1")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.deletePost(1L, member1));

        assertEquals(exception.getErrorCode(), FAIL_TO_DELETE_POST);
        assertEquals(exception.getMessage(), FAIL_TO_DELETE_POST.getMessage());
    }

    @Test
    @DisplayName("이미지 등록 성공")
    void addPostImage_Success() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        MockMultipartFile file = new MockMultipartFile("test", "test_2023.png", "image/png", "test".getBytes());
        String imgUrl = "https://image-bucket.s3.abc.jpg";

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(awsS3Service.uploadFile(any(), anyString())).willReturn(imgUrl);

        ArgumentCaptor<PostImage> postImageCaptor = ArgumentCaptor.forClass(PostImage.class);

        PostImageResponse response = postService.addPostImage(1L, file);

        assertEquals(response.getPostImageUrl(), imgUrl);
        verify(postImageRepository, times(1)).save(postImageCaptor.capture());
    }

    @Test
    @DisplayName("이미지 등록 실패")
    void addPostImage_Fail() {

        MockMultipartFile file = new MockMultipartFile("test", "test_2023.png", "image/png", "test".getBytes());

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception = assertThrows(BoardException.class,
                () -> postService.addPostImage(1L, file));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이미지 삭제 성공")
    void deletePostImage_Success() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        PostImage postImage = PostImage.builder()
                .postImageUrl("https://image-bucket.s3.abc.jpg")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postImageRepository.findById(anyLong())).willReturn(Optional.of(postImage));

        Map<String, String> response = postService.deletePostImage(1L, 1L);

        assertNotNull(response.get("result"));
    }

    @Test
    @DisplayName("이미지 삭제 실패1")
    void deletePostImage_Fail1() {

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception  = assertThrows(BoardException.class,
                () -> postService.deletePostImage(1L, 1L));

        assertEquals(exception.getErrorCode(), POST_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이미지 삭제 실패2")
    void deletePostImage_Fail2() {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postImageRepository.findById(anyLong())).willReturn(Optional.empty());

        BoardException exception  = assertThrows(BoardException.class,
                () -> postService.deletePostImage(1L, 1L));

        assertEquals(exception.getErrorCode(), POST_IMAGE_NOT_FOUND);
        assertEquals(exception.getMessage(), POST_IMAGE_NOT_FOUND.getMessage());
    }
}