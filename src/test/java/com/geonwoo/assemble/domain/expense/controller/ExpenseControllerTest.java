package com.geonwoo.assemble.domain.expense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartyJdbcRepository partyJdbcRepository;

    @Autowired
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Autowired
    MemberJdbcRepository memberJdbcRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    String token;

    Long partyId;
    Long payerPartyMemberId1;
    Long partyMemberId2;

    @BeforeEach
    void setUp() {
        Member member1 = new Member("loginId", "password", "email");
        Long memberId1 = memberJdbcRepository.save(member1);
        token = jwtTokenProvider.createToken(memberId1, member1.getRole());

        Member member2 = new Member("loginId2", "password2", "email2");
        Long memberId2 = memberJdbcRepository.save(member2);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        PartyMember partyMember1 = new PartyMember(partyId, memberId1, PartyMemberRole.MEMBER);
        payerPartyMemberId1 = partyMemberJdbcRepository.save(partyMember1);

        PartyMember partyMember2 = new PartyMember(partyId, memberId2, PartyMemberRole.MEMBER);
        partyMemberId2 = partyMemberJdbcRepository.save(partyMember2);
    }

    @Test
    @Transactional
    void save() throws Exception {

        ExpenseSaveDTO expenseSaveDTO = new ExpenseSaveDTO(partyId, payerPartyMemberId1, 1000, "1차 회식비용", List.of(partyMemberId2));

        String json = objectMapper.writeValueAsString(expenseSaveDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/partys/{partyId}/expense", partyId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/partys/" + partyId + "/expense/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/partys/" + partyId + "/expense/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }
}