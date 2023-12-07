package com.geonwoo.assemble.domain.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.dto.PartyCreateDTO;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    JwtTokenProvider jwtTokenProvider;

    String token;

    @BeforeAll
    void setUp() {
        Member member = new Member("loginId", "password", "email");
        memberJdbcRepository.save(member);
        token = jwtTokenProvider.createToken(member.getId(), member.getRole());
    }

    @Test
    @Transactional
    void save() throws Exception {

        PartyCreateDTO partyCreateDTO = new PartyCreateDTO("name", "content", LocalDate.now());
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
    void findById() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.get("/partys/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(party.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(party.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value(party.getStartDate().toString()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void update() throws Exception {

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
    void delete() throws Exception {

        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.delete("/partys/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}