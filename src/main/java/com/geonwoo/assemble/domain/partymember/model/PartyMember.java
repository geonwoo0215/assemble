package com.geonwoo.assemble.domain.partymember.model;

import lombok.Getter;

@Getter
public class PartyMember {

    private Long id;
    private Long partyId;
    private Long memberId;

    public PartyMember(Long partyId, Long memberId) {
        this.partyId = partyId;
        this.memberId = memberId;
    }
}
