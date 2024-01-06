package com.geonwoo.assemble.domain.expense.exception;

import com.geonwoo.assemble.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ExpenseNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "비용데이터 조회를 실패하였습니다.";

    public ExpenseNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
