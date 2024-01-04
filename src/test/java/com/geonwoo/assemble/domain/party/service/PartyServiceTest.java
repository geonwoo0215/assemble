package com.geonwoo.assemble.domain.party.service;

import com.geonwoo.assemble.domain.imageurl.repository.ImageUrlJdbcRepository;
import com.geonwoo.assemble.domain.party.dto.PartySaveDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyServiceTest {

    @Mock
    PartyJdbcRepository partyJdbcRepository;

    @Mock
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Mock
    ImageUrlJdbcRepository imageUrlJdbcRepository;

    @InjectMocks
    PartyService partyService;

    @Test
    void 모임_저장_성공() {

        Long userId = 1L;
        Long partyId = 1L;
        Long partyMemberId = 1L;
        String name = "name";
        String content = "content";

        PartySaveDTO partySaveDTO = new PartySaveDTO(name, content, LocalDate.now());
        Mockito.when(partyJdbcRepository.save(Mockito.any(Party.class))).thenReturn(partyId);
        Mockito.when(partyMemberJdbcRepository.save(Mockito.any(PartyMember.class))).thenReturn(partyMemberId);

        partyService.save(userId, partySaveDTO);

        Mockito.verify(partyJdbcRepository).save(Mockito.any(Party.class));
        Mockito.verify(partyMemberJdbcRepository).save(Mockito.any(PartyMember.class));
    }

    @Test
    void 사용자아이디로_모임_조회_성공() {

        Long userId = 1L;
        int size = 10;
        int offset = 0;

        Party party = new Party("name", "content", LocalDate.now());

        Mockito.when(partyJdbcRepository.findAllByMemberId(userId, size, offset)).thenReturn(List.of(party));

        partyService.findAllByMemberId(userId, size, offset);

        Mockito.verify(partyJdbcRepository).findAllByMemberId(userId, size, offset);

    }

    @Test
    void 모임아이디로_모임_조회_성공() {

        Long partyId = 1L;
        Party party = new Party("name", "content", LocalDate.now());

        Mockito.when(imageUrlJdbcRepository.findByPartyId(partyId)).thenReturn(List.of("url"));
        Mockito.when(partyJdbcRepository.findById(partyId)).thenReturn(Optional.of(party));

        partyService.findById(partyId);

        Mockito.verify(imageUrlJdbcRepository).findByPartyId(partyId);
        Mockito.verify(partyJdbcRepository).findById(partyId);

    }
}