package com.geonwoo.assemble.domain.invitation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.invitation.model.Invitation;
import com.geonwoo.assemble.domain.invitation.repository.InvitationJdbcRepository;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InvitationControllerTest {


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

    @Autowired
    InvitationJdbcRepository invitationJdbcRepository;

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
    void 초대코드_생성_API_성공() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/partys/{partyId}/invitation", partyId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 초대코드_검증_API_성공() throws Exception {

        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2));
        invitationJdbcRepository.save(invitation);

        mockMvc.perform(MockMvcRequestBuilders.get("/invite/{inviteCode}", invitation.getInviteCode())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

}