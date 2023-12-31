package com.geonwoo.assemble.domain.expensecomment.model;

import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseComment {

    private Long id;
    private Long expenseId;
    private Long partyMemberId;
    private String comment;
    private Long groupNo;
    private Long depth;
    private Long commentOrder;
    private Long parentId;

    public ExpenseComment(Long expenseId, Long partyMemberId, String comment, Long groupNo, Long depth, Long commentOrder, Long parentId) {
        this.expenseId = expenseId;
        this.partyMemberId = partyMemberId;
        this.comment = comment;
        this.groupNo = groupNo;
        this.depth = depth;
        this.commentOrder = commentOrder;
        this.parentId = parentId;
    }

    public ExpenseCommentDTO toExpenseCommentDTO() {
        return new ExpenseCommentDTO(id, comment, depth);
    }
}
