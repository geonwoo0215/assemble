package com.geonwoo.assemble.domain.partymember.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.dto.PartyMemberSaveDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
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

@SpringBootTest
@AutoConfigureMockMvc
class PartyMemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberJdbcRepository memberJdbcRepository;

    @Autowired
    PartyJdbcRepository partyJdbcRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Long memberId;

    Long partyId;

    String token;

    @BeforeEach
    void setUp() {
        Member member = new Member("loginId", "password", "email");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        token = jwtTokenProvider.createToken(memberId, member.getRole());
    }


    @Test
    @Transactional
    void save() throws Exception {

        PartyMemberSaveDTO partyMemberSaveDTO = new PartyMemberSaveDTO(partyId, memberId, PartyMemberRole.MEMBER);
        String json = objectMapper.writeValueAsString(partyMemberSaveDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/partyMembers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/partyMembers/")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());

    }
}