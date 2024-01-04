package com.geonwoo.assemble.domain.partymember.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import com.geonwoo.assemble.global.aws.s3.service.S3Service;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    S3Client s3Client;

    @MockBean
    S3Service s3Service;

    Long memberId;

    Long partyId;

    String token;

    @BeforeEach
    void setUp() {
        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        token = jwtTokenProvider.createToken(memberId, member.getRole());
    }


    @Test
    @Transactional
    void 모임원_저장_API_성공() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/partys/{partyId}/partyMembers", partyId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/partys/" + partyId + "/partyMembers")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Transactional
    void 모임아이디로_모임원_API_조회() throws Exception {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        partyMemberJdbcRepository.save(partyMember);

        mockMvc.perform(MockMvcRequestBuilders.get("/partys/{partyId}/partyMembers", partyId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 모임원_삭제_API_성공() throws Exception {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);

        mockMvc.perform(MockMvcRequestBuilders.delete("/partys/{partyId}/partyMembers/{id}", partyId, id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}