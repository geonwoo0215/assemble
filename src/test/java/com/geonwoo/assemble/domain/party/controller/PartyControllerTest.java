package com.geonwoo.assemble.domain.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.dto.PartySaveDTO;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
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
import org.springframework.util.LinkedMultiValueMap;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartyJdbcRepository partyJdbcRepository;

    @Autowired
    MemberJdbcRepository memberJdbcRepository;

    @Autowired
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    S3Client s3Client;

    @MockBean
    S3Service s3Service;

    String token;

    Long memberId;

    @BeforeEach
    void setUp() {
        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);
        token = jwtTokenProvider.createToken(memberId, member.getRole());
    }

    @Test
    @Transactional
    void 모임_저장_API_성공() throws Exception {

        PartySaveDTO partyCreateDTO = new PartySaveDTO("name", "content", LocalDate.now());
        String json = objectMapper.writeValueAsString(partyCreateDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/partys")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/partys/*"))
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/partys/")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 모임아이디로_모임_조회_API_성공() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.get("/partys/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(party.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(party.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.eventDate").value(party.getEventDate().toString()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 사용자아이디로_사용자가포함된모임전체_조회_API_성공() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long partyId = partyJdbcRepository.save(party);
        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.LEADER);
        partyMemberJdbcRepository.save(partyMember);

        int page = 0;
        int size = 10;
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));
        mockMvc.perform(MockMvcRequestBuilders.get("/partys")
                        .params(params)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 모임_수정_API_성공() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        PartyUpdateDTO partyUpdateDTO = new PartyUpdateDTO("updateName", "updateContent", LocalDate.now().plusDays(1));
        String json = objectMapper.writeValueAsString(partyUpdateDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/partys/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Transactional
    void 모임_삭제_API_성공() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.delete("/partys/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}