package com.geonwoo.assemble.domain.expensecomment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentSaveDTO;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.repository.PartyMemberExpenseJdbcRepository;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import com.geonwoo.assemble.global.aws.s3.service.S3Service;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
class ExpenseCommentControllerTest {

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
    ExpenseJdbcRepository expenseJdbcRepository;

    @Autowired
    PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    S3Client s3Client;

    @MockBean
    S3Service s3Service;

    String token;
    Long expenseId;
    Long partyId;
    Long payerPartyMemberId1;

    @BeforeEach
    void setUp() {
        Member member1 = new Member("loginId", "password", "email", "nickname");
        Long memberId1 = memberJdbcRepository.save(member1);
        token = jwtTokenProvider.createToken(memberId1, member1.getRole());

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        PartyMember partyMember1 = new PartyMember(partyId, memberId1, PartyMemberRole.MEMBER);
        payerPartyMemberId1 = partyMemberJdbcRepository.save(partyMember1);

        Expense expense = new Expense(partyId, 1000, "content");
        expenseId = expenseJdbcRepository.save(expense);
    }

    @Test
    @Transactional
    void 비용_댓글_저장_API_성공() throws Exception {

        ExpenseCommentSaveDTO expenseCommentSaveDTO = new ExpenseCommentSaveDTO(0L, payerPartyMemberId1, "comment");

        String json = objectMapper.writeValueAsString(expenseCommentSaveDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/partys/{partyId}/expense/{expenseId}/comment", partyId, expenseId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/partys/" + partyId + "/expense/" + expenseId + "/comment")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/partys/" + partyId + "/expense/" + expenseId + "/comment/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 비용_아이디로_비용_댓글_조회_API_성공() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/partys/{partyId}/expense/{expenseId}/comment", partyId, expenseId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());

    }
}