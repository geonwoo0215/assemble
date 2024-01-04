package com.geonwoo.assemble.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.member.dto.MemberSignUpDTO;
import com.geonwoo.assemble.global.aws.s3.service.S3Service;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    S3Client s3Client;

    @MockBean
    S3Service s3Service;

    @Test
    @Transactional
    void 회원가입_API_성공() throws Exception {

        MemberSignUpDTO memberSignUpDTO = new MemberSignUpDTO("loginId", "password", "email", "nickname");
        String json = objectMapper.writeValueAsString(memberSignUpDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/members/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/members/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

}