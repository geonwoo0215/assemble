package com.geonwoo.assemble.domain.partymember.service;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTOs;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyMemberService {

    private final PartyMemberJdbcRepository repository;

    @Transactional
    public Long save(Long memberId, Long partyId) {
        return repository.findByMemberIdAndPartyId(memberId, partyId)
                .map(PartyMember::getId)
                .orElseGet(() -> {
                    PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
                    return repository.save(partyMember);
                });
    }

    @Transactional
    public PartyMemberDTOs findAllByPartyId(Long userId, Long partyId) {

        List<PartyMemberDTO> partymemberDTOList = repository.findAllByPartyId(partyId);

        PartyMemberDTO partyMemberDTO = partymemberDTOList.stream()
                .filter(pm -> Objects.equals(pm.getMemberId(), userId))
                .findAny()
                .orElseThrow(RuntimeException::new);


        List<PartyMemberDTO> list = partymemberDTOList
                .stream()
                .filter(pm -> !Objects.equals(pm.getMemberId(), userId))
                .collect(Collectors.toList());

        PartyMemberDTOs partyMemberDTOs = new PartyMemberDTOs(partyMemberDTO.getId(), partyMemberDTO.getNickname(), list);
        return partyMemberDTOs;
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }

}
