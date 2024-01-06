package com.geonwoo.assemble.domain.invitation.exception;

import com.geonwoo.assemble.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvitationNotFoundException extends CustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String MESSAGE = "초대장 조회를 실패하였습니다.";

    public InvitationNotFoundException(Object... params) {
        super(STATUS, params, MESSAGE);
    }
}
