package com.geonwoo.assemble.domain.partymember.service;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyMemberService {

    private final PartyMemberJdbcRepository repository;

    @Transactional
    public Long save(Long memberId, Long partyId) {
        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = repository.save(partyMember);
        return id;
    }

    @Transactional
    public List<PartyMemberDTO> findAllByPartyId(Long partyId) {
        List<PartyMemberDTO> list = repository.findAllByPartyId(partyId)
                .stream().map(PartyMember::toPartyMemberDTO)
                .toList();
        return list;
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }

}
