package com.geonwoo.assemble.domain.partymember.dto;

import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import lombok.Getter;

@Getter
public class PartyMemberSaveDTO {

    private Long partyId;
    private Long memberId;
    private PartyMemberRole partyMemberRole;

    public PartyMemberSaveDTO(Long partyId, Long memberId, PartyMemberRole partyMemberRole) {
        this.partyId = partyId;
        this.memberId = memberId;
        this.partyMemberRole = partyMemberRole;
    }

    public PartyMember toPartyMember() {
        return new PartyMember(partyId, memberId, partyMemberRole);
    }
}
