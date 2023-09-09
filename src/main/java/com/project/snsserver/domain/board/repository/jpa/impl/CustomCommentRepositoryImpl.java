package com.project.snsserver.domain.board.repository.jpa.impl;

import com.project.snsserver.domain.board.model.dto.CommentResponse;
import com.project.snsserver.domain.board.model.entity.Comment;
import com.project.snsserver.domain.board.repository.jpa.CustomCommentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.snsserver.domain.board.model.entity.QComment.comment;
import static com.project.snsserver.domain.board.model.entity.QPost.post;
import static com.project.snsserver.domain.member.model.entity.QMember.member;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;


    /**
     * 특정 게시물의 댓글 조회
     */
    @Override
    public Slice<CommentResponse> findCommentAllByPostId(Long postId, Long lastCommentId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment)
                .leftJoin(comment.post, post).fetchJoin()
                .leftJoin(comment.member, member).fetchJoin()
                .where(
                        lastCommentId(lastCommentId),
                        comment.post.id.eq(postId)
                )
                .orderBy(comment.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, comments);
    }

    /**
     * 특정 게시물의 댓글 전체 삭제
     */
    @Override
    public Long deleteCommentAllByPostId(Long postId) {
        return queryFactory.delete(comment)
                .where(comment.post.id.eq(postId))
                .execute();
    }

    @Override
    public Long deleteCommentAllByMemberId(Long memberId) {
        return queryFactory.delete(comment)
                .where(comment.member.id.eq(memberId))
                .execute();
    }

    public Long deleteCommentAllInPostIdsByMemberId(Long memberId) {
        return queryFactory.delete(comment)
                .where(comment.post.id.in(
                                select(post.id).from(post).where(post.member.id.eq(memberId))
                        )
                )
                .execute();
    }

    private BooleanExpression lastCommentId(Long lastCommentId) {
        if (lastCommentId == null) {
            return null;
        }
        return comment.id.lt(lastCommentId);
    }

    private Slice<CommentResponse> checkLastPage(Pageable pageable, List<Comment> comments) {
        boolean hasNext = false;

        // 조회한 댓글의 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음
        if (comments.size() > pageable.getPageSize()) {
            hasNext = true;
            comments.remove(pageable.getPageSize());
        }

        List<CommentResponse> commentsByPost
                = comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());

        return new SliceImpl<>(commentsByPost, pageable, hasNext);
    }
}
