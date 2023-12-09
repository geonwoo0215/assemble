package com.geonwoo.assemble.domain.partymember.dto;

import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import lombok.Getter;

@Getter
public class PartyMemberDTO {

    private Long id;
    private Long partyId;
    private Long memberId;
    private PartyMemberRole role;

    public PartyMemberDTO(Long id, Long partyId, Long memberId, PartyMemberRole role) {
        this.id = id;
        this.partyId = partyId;
        this.memberId = memberId;
        this.role = role;
    }
}
