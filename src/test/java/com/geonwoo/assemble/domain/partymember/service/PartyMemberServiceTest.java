package com.geonwoo.assemble.domain.partymember.service;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyMemberServiceTest {

    @Mock
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @InjectMocks
    PartyMemberService partyMemberService;

    @Test
    void 모임아이디로_모임원_조회_성공() {

        Long userId = 1L;
        Long partyId = 1L;


        List<PartyMemberDTO> partyMemberDTOList = IntStream.range(1, 5)
                .mapToObj(i -> {
                    PartyMemberDTO partyMemberDTO = new PartyMemberDTO((long) i, partyId, (long) i, PartyMemberRole.MEMBER, "nickname");
                    partyMemberJdbcRepository.findAllByPartyId(partyId);
                    return partyMemberDTO;
                }).toList();

        Mockito.when(partyMemberJdbcRepository.findAllByPartyId(partyId)).thenReturn(partyMemberDTOList);

        partyMemberService.findAllByPartyId(userId, partyId);

        Mockito.verify(partyMemberJdbcRepository, Mockito.times(5)).findAllByPartyId(partyId);

    }
}