package com.geonwoo.assemble.domain.expensecomment.dto;

import lombok.Getter;

@Getter
public class ExpenseCommentSaveDTO {

    private Long commentId;
    private Long partyMemberId;
    private String comment;
}
