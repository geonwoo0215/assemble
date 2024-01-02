package com.geonwoo.assemble.domain.party.service;

import com.geonwoo.assemble.domain.imageurl.repository.ImageUrlRepository;
import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.dto.PartyDetailDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyJdbcRepository partyJdbcRepository;
    private final PartyMemberJdbcRepository partyMemberJdbcRepository;
    private final ImageUrlRepository imageUrlRepository;

    @Transactional
    public Long save(Long userId, PartySaveDTO partySaveDTO) {

        Party party = partySaveDTO.toParty();
        Long id = partyJdbcRepository.save(party);

        PartyMember partyMember = new PartyMember(id, userId, PartyMemberRole.LEADER);
        partyMemberJdbcRepository.save(partyMember);
        return id;
    }

    public List<PartyDTO> findAllByMemberId(Long memberId, int size, int offset) {
        List<PartyDTO> partyDTOList = partyJdbcRepository.findAllByMemberId(memberId, size, offset)
                .stream()
                .map(Party::toPartyDTO)
                .toList();

        return partyDTOList;
    }

    public PartyDetailDTO findById(Long id) {

        List<String> imageUrls = imageUrlRepository.findByPartyId(id);

        PartyDetailDTO partyDetailDTO = partyJdbcRepository.findById(id)
                .map(a -> a.toPartyDetailDTO(imageUrls))
                .orElseThrow(RuntimeException::new);

        return partyDetailDTO;
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
