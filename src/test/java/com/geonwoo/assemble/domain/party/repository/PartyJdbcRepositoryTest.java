package com.geonwoo.assemble.domain.party.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
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
import java.util.List;
import java.util.Optional;

@JdbcTest
@Sql("classpath:schema.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    PartyJdbcRepository partyJdbcRepository;

    MemberJdbcRepository memberJdbcRepository;

    PartyMemberJdbcRepository partyMemberJdbcRepository;

    Long memberId;

    @BeforeEach
    void setUp() {
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        memberJdbcRepository = new MemberJdbcRepository(dataSource);
        partyMemberJdbcRepository = new PartyMemberJdbcRepository(dataSource);

        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);
    }

    @Test
    void 모임_저장_성공() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 모임_아이디로_조회_성공() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        Optional<Party> optionalParty = partyJdbcRepository.findById(id);
        Assertions.assertThat(optionalParty).isPresent();
        Party saveParty = optionalParty.get();
        Assertions.assertThat(saveParty)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", party.getName())
                .hasFieldOrPropertyWithValue("content", party.getContent())
                .hasFieldOrPropertyWithValue("eventDate", party.getEventDate());

    }

    @Test
    void 사용자_아이디로_모임_조회_성공() {

        Party party = new Party("name", "content", LocalDate.now());
        Long partyId = partyJdbcRepository.save(party);

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.LEADER);
        partyMemberJdbcRepository.save(partyMember);

        int limit = 10;
        int offset = 0;

        List<Party> list = partyJdbcRepository.findAllByMemberId(memberId, limit, offset);

        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void 모임_수정_성공() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        PartyUpdateDTO partyUpdateDTO = new PartyUpdateDTO("updateDTO", "updateContent", LocalDate.now().minusDays(1L));
        partyJdbcRepository.update(id, partyUpdateDTO);

        Optional<Party> optionalParty = partyJdbcRepository.findById(id);
        Assertions.assertThat(optionalParty).isPresent();
        Party saveParty = optionalParty.get();
        Assertions.assertThat(saveParty)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", partyUpdateDTO.getName())
                .hasFieldOrPropertyWithValue("content", partyUpdateDTO.getContent())
                .hasFieldOrPropertyWithValue("eventDate", partyUpdateDTO.getEventDate());
    }

    @Test
    void 모임_삭제_성공() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);
        partyJdbcRepository.delete(id);

        Optional<Party> optionalParty = partyJdbcRepository.findById(id);
        Assertions.assertThat(optionalParty).isEmpty();
    }
}