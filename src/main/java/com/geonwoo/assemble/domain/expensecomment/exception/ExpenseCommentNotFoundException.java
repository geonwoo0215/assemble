package com.geonwoo.assemble.domain.expensecomment.exception;

import com.geonwoo.assemble.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ExpenseCommentNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "댓글 조회를 실패하였습니다.";

    public ExpenseCommentNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
