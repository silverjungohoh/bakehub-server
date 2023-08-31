package com.project.snsserver.global.error.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 게시판에 대한 예외 CODE = E401 ~ E499
 */

@Getter
@AllArgsConstructor
public enum BoardErrorCode {

    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "E401", "이미지는 최대 5장까지 업로드할 수 있습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E402", "존재하지 않는 게시물입니다."),
    FAIL_TO_UPDATE_POST(HttpStatus.UNAUTHORIZED, "E403", "게시물 수정 권한은 작성자에게 있습니다."),
    FAIL_TO_DELETE_POST(HttpStatus.UNAUTHORIZED, "E404", "게시물 삭제 권한은 작성자에게 있습니다."),
    POST_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E405", "존재하지 않는 이미지입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E406", "존재하지 않는 댓글입니다."),
    FAIL_TO_DELETE_COMMENT(HttpStatus.UNAUTHORIZED, "E407", "댓글 삭제 권한은 작성자에게 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
