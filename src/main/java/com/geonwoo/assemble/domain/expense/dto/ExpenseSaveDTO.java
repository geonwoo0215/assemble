package com.geonwoo.assemble.domain.expense.dto;

import com.geonwoo.assemble.domain.expense.model.Expense;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExpenseSaveDTO {

    @NotNull(message = "결제자 아이디를 입력해주세요.")
    private Long payerPartyMemberId;

    @PositiveOrZero(message = "가격을 입력해주세요.")
    private Integer price;

    private String content;

    private List<Long> partyMemberIds;

    private List<String> imageUrls;

    public ExpenseSaveDTO(Long payerPartyMemberId, Integer price, String content, List<Long> partyMemberIds, List<String> imageUrls) {
        this.payerPartyMemberId = payerPartyMemberId;
        this.price = price;
        this.content = content;
        this.partyMemberIds = partyMemberIds;
        this.imageUrls = imageUrls;
    }

    public Expense toExpense(Long partyId) {
        return new Expense(partyId, price, content);
    }
}
