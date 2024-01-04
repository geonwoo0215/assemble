package com.geonwoo.assemble.domain.expensecomment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseCommentSaveDTO {

    private Long commentId;
    private Long partyMemberId;
    private String comment;

    public ExpenseCommentSaveDTO(Long commentId, Long partyMemberId, String comment) {
        this.commentId = commentId;
        this.partyMemberId = partyMemberId;
        this.comment = comment;
    }
}
