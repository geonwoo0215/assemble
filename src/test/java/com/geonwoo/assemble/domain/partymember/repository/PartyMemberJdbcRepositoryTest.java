package com.geonwoo.assemble.domain.partymember.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Optional;

@JdbcTest
@Sql("classpath:schema.sql")
class PartyMemberJdbcRepositoryTest {

    MemberJdbcRepository memberJdbcRepository;
    PartyJdbcRepository partyJdbcRepository;
    PartyMemberJdbcRepository partyMemberJdbcRepository;

    @Autowired
    DataSource dataSource;

    Long memberId;
    Long partyId;

    @BeforeEach
    void setUp() {
        memberJdbcRepository = new MemberJdbcRepository(dataSource);
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        partyMemberJdbcRepository = new PartyMemberJdbcRepository(dataSource);

        Member member = new Member("loginId", "password", "email");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

    }

    @Test
    void save() {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void delete() {
        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);
        partyMemberJdbcRepository.delete(id);

        Optional<PartyMember> optionalPartyMember = partyMemberJdbcRepository.findById(id);
        Assertions.assertThat(optionalPartyMember).isEmpty();
    }


}