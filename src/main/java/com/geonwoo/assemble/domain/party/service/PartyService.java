package com.geonwoo.assemble.domain.party.service;

import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.dto.PartySaveDTO;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyJdbcRepository partyJdbcRepository;
    private final PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Transactional
    public Long save(Long userId, PartySaveDTO partyCreateDTO) {

        Party party = partyCreateDTO.toParty();
        Long id = partyJdbcRepository.save(party);
        
        PartyMember partyMember = new PartyMember(id, userId, PartyMemberRole.LEADER);
        partyMemberJdbcRepository.save(partyMember);
        return id;
    }

    public PartyDTO findById(Long id) {

        PartyDTO partyDTO = partyJdbcRepository.findById(id)
                .map(Party::toPartyDTO)
                .orElseThrow(RuntimeException::new);

        return partyDTO;
    }

    @Transactional
    public void update(Long id, PartyUpdateDTO partyUpdateDTO) {
        partyJdbcRepository.update(id, partyUpdateDTO);
    }

    @Transactional
    public void delete(Long id) {
        partyJdbcRepository.delete(id);
    }
}
