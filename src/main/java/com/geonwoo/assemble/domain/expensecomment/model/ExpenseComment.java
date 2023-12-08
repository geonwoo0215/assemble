package com.geonwoo.assemble.domain.expensecomment.model;

import lombok.Getter;

@Getter
public class ExpenseComment {

    private Long id;
    private Long expenseId;
    private Long partyMemberId;
    private String comment;

    public ExpenseComment(Long expenseId, Long partyMemberId, String comment) {
        this.expenseId = expenseId;
        this.partyMemberId = partyMemberId;
        this.comment = comment;
    }
}
