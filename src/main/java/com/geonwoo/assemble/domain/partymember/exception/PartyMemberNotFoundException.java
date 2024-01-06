package com.geonwoo.assemble.domain.partymember.exception;

import com.geonwoo.assemble.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PartyMemberNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "모임원 조회를 실패하였습니다.";

    public PartyMemberNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
