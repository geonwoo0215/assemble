package com.geonwoo.assemble.domain.partymember.service;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberSaveDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyMemberService {

    private final PartyMemberJdbcRepository repository;

    @Transactional
    public Long save(PartyMemberSaveDTO partyMemberSaveDTO) {
        PartyMember partyMember = partyMemberSaveDTO.toPartyMember();
        Long id = repository.save(partyMember);
        return id;
    }
}
