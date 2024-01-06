package com.geonwoo.assemble.domain.expensecomment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseCommentSaveDTO {

    @NotNull(message = "부모 댓글 아이디를 입력해주세요, 없으면 0을 입력해주세요.")
    private Long commentId;

    @NotNull(message = "댓글 작성자의 모임원 아이디를 입력해주세요")
    private Long partyMemberId;

    @NotBlank(message = "댓글을 입력해주세요.")
    private String comment;

    public ExpenseCommentSaveDTO(Long commentId, Long partyMemberId, String comment) {
        this.commentId = commentId;
        this.partyMemberId = partyMemberId;
        this.comment = comment;
    }
}
