package com.geonwoo.assemble.domain.partymemberexpense.model;

import lombok.Getter;

@Getter
public class PartyMemberExpense {

    private Long id;
    private Long partyId;
    private Long partyMemberId;
    private boolean payer;

    public PartyMemberExpense(Long partyId, Long partyMemberId, boolean payer) {
        this.partyId = partyId;
        this.partyMemberId = partyMemberId;
        this.payer = payer;
    }
}
