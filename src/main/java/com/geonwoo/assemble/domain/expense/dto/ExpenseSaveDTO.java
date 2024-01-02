package com.geonwoo.assemble.domain.expense.dto;

import com.geonwoo.assemble.domain.expense.model.Expense;
import lombok.Getter;

import java.util.List;

@Getter
public class ExpenseSaveDTO {

    private Long payerPartyMemberId;
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
