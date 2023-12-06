package com.geonwoo.assemble.domain.party.service;

import com.geonwoo.assemble.domain.party.dto.PartyCreateDTO;
import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyJdbcRepository partyJdbcRepository;


    @Transactional
    public Long save(PartyCreateDTO partyCreateDTO) {

        Party party = partyCreateDTO.toParty();
        Long id = partyJdbcRepository.save(party);

        return id;
    }

    public PartyDTO findById(Long id) {

        PartyDTO partyDTO = partyJdbcRepository.findById(id)
                .map(Party::toPartyDTO)
                .orElseThrow(RuntimeException::new);

        return partyDTO;
    }

    @Transactional
    public PartyDTO update(Long id, PartyUpdateDTO partyUpdateDTO) {

        partyJdbcRepository.update(id, partyUpdateDTO);
        PartyDTO partyDTO = new PartyDTO(id, partyUpdateDTO.getName(), partyUpdateDTO.getContent(), partyUpdateDTO.getStartDate());

        return partyDTO;
    }
}
