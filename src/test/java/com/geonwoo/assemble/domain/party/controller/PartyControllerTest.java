package com.geonwoo.assemble.domain.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.party.dto.PartyCreateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
class PartyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartyJdbcRepository partyJdbcRepository;

    @Test
    @Transactional
    void save() throws Exception {

        PartyCreateDTO partyCreateDTO = new PartyCreateDTO("name", "content", LocalDate.now());
        String json = objectMapper.writeValueAsString(partyCreateDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/partys")
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
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(party.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(party.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value(party.getStartDate().toString()))
                .andDo(MockMvcResultHandlers.print());
    }
}