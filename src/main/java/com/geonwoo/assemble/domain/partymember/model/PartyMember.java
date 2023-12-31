package com.geonwoo.assemble.domain.partymember.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartyMember {

    private Long id;
    private Long partyId;
    private Long memberId;
    private PartyMemberRole role;

    public PartyMember(Long partyId, Long memberId, PartyMemberRole role) {
        this.partyId = partyId;
        this.memberId = memberId;
        this.role = role;
    }
}
