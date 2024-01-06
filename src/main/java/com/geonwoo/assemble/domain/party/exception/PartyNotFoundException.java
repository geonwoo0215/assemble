package com.geonwoo.assemble.domain.party.exception;

import com.geonwoo.assemble.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PartyNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "모임 조회를 실패하였습니다.";

    public PartyNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
