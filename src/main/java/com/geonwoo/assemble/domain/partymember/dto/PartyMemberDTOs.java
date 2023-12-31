package com.geonwoo.assemble.domain.partymember.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PartyMemberDTOs {

    private Long currentMemberPartyMemberId;
    private String currentMemberNickname;
    private List<PartyMemberDTO> partyMemberDTOList;

    public PartyMemberDTOs(Long currentMemberPartyMemberId, String currentMemberNickname, List<PartyMemberDTO> partyMemberDTOList) {
        this.currentMemberPartyMemberId = currentMemberPartyMemberId;
        this.currentMemberNickname = currentMemberNickname;
        this.partyMemberDTOList = partyMemberDTOList;
    }
}
