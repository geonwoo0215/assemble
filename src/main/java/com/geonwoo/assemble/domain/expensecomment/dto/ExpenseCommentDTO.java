package com.geonwoo.assemble.domain.expensecomment.dto;

import lombok.Getter;

@Getter
public class ExpenseCommentDTO {
    private Long id;
    private String comment;
    private Long depth;

    public ExpenseCommentDTO(Long id, String comment, Long depth) {
        this.id = id;
        this.comment = comment;
        this.depth = depth;
    }
}
