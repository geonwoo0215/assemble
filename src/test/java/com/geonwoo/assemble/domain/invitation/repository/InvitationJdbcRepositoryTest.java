package com.geonwoo.assemble.domain.invitation.repository;

import com.geonwoo.assemble.domain.invitation.model.Invitation;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@JdbcTest
@Sql("classpath:schema.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InvitationJdbcRepositoryTest {

    InvitationJdbcRepository invitationJdbcRepository;

    PartyJdbcRepository partyJdbcRepository;

    @Autowired
    DataSource dataSource;

    Long partyId;

    @BeforeEach
    void setUp() {

        invitationJdbcRepository = new InvitationJdbcRepository(dataSource);
        partyJdbcRepository = new PartyJdbcRepository(dataSource);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

    }

    @Test
    void 초대장_저장_성공() {
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2));
        Long id = invitationJdbcRepository.save(invitation);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 초대코드로_초대장_조회_성공() {
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2).withNano(0));
        invitationJdbcRepository.save(invitation);
        Optional<Invitation> optionalInvitation = invitationJdbcRepository.findByInviteCode(invitation.getInviteCode());

        Assertions.assertThat(optionalInvitation).isPresent();
        Invitation findInvitation = optionalInvitation.get();

        Assertions.assertThat(findInvitation)
                .hasFieldOrPropertyWithValue("partyId", invitation.getPartyId())
                .hasFieldOrPropertyWithValue("expiredDate", invitation.getExpiredDate().withNano(0))
                .hasFieldOrPropertyWithValue("inviteCode", invitation.getInviteCode());
    }

    @Test
    void 모임아이디로_초대장_조회_성공() {
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2).withNano(0));
        invitationJdbcRepository.save(invitation);

        Optional<Invitation> optionalInvitation = invitationJdbcRepository.findByPartyId(partyId);

        Assertions.assertThat(optionalInvitation).isPresent();
        Invitation findInvitation = optionalInvitation.get();

        Assertions.assertThat(findInvitation)
                .hasFieldOrPropertyWithValue("partyId", invitation.getPartyId())
                .hasFieldOrPropertyWithValue("expiredDate", invitation.getExpiredDate().withNano(0))
                .hasFieldOrPropertyWithValue("inviteCode", invitation.getInviteCode());
    }


}