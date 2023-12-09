package com.geonwoo.assemble.domain.partymemberexpense.model;

import lombok.Getter;

@Getter
public class PartyMemberExpense {

    private Long id;
    private Long expenseId;
    private Long partyMemberId;
    private boolean payer;

    public PartyMemberExpense(Long expenseId, Long partyMemberId, boolean payer) {
        this.expenseId = expenseId;
        this.partyMemberId = partyMemberId;
        this.payer = payer;
    }
}
