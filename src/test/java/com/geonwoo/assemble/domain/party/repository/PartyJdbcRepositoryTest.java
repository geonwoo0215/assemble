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
class PartyJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    PartyJdbcRepository partyJdbcRepository;

    MemberJdbcRepository memberJdbcRepository;

    PartyMemberJdbcRepository partyMemberJdbcRepository;

    Long memberId;

    Long partyId;
    Long partyMemberId;

    @BeforeEach
    void setUp() {
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        memberJdbcRepository = new MemberJdbcRepository(dataSource);
        partyMemberJdbcRepository = new PartyMemberJdbcRepository(dataSource);

        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);

    }

    @Test
    void save() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void findById() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);

        Optional<Party> optionalParty = partyJdbcRepository.findById(id);
        Assertions.assertThat(optionalParty).isPresent();
        Party saveParty = optionalParty.get();
        Assertions.assertThat(saveParty)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", party.getName())
                .hasFieldOrPropertyWithValue("content", party.getContent())
                .hasFieldOrPropertyWithValue("startDate", party.getStartDate());

    }

    @Test
    void findAllByMemberId() {

        Party party = new Party("name", "content", LocalDate.now());
        Long partyId = partyJdbcRepository.save(party);

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.LEADER);
        partyMemberJdbcRepository.save(partyMember);

        List<Party> list = partyJdbcRepository.findAllByMemberId(memberId);

        Assertions.assertThat(list.size()).isEqualTo(1);

    }

    @Test
    void update() {
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
                .hasFieldOrPropertyWithValue("startDate", partyUpdateDTO.getStartDate());
    }

    @Test
    void delete() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = partyJdbcRepository.save(party);
        partyJdbcRepository.delete(id);

        Optional<Party> optionalParty = partyJdbcRepository.findById(id);
        Assertions.assertThat(optionalParty).isEmpty();
    }
}