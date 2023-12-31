package com.geonwoo.assemble.domain.partymember.dto;

import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMemberDTO {

    private Long id;
    private Long partyId;
    private Long memberId;
    private PartyMemberRole role;
    private String nickname;

    public PartyMemberDTO(Long id, Long partyId, Long memberId, PartyMemberRole role, String nickname) {
        this.id = id;
        this.partyId = partyId;
        this.memberId = memberId;
        this.role = role;
        this.nickname = nickname;
    }
}
