package com.geonwoo.assemble.domain.partymember.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
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

        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

    }

    @Test
    void 모임원_저장_성공() {
        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 모임원아이디로_모임원_조회_성공() {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);
        Optional<PartyMember> optionalPartyMember = partyMemberJdbcRepository.findById(id);

        Assertions.assertThat(optionalPartyMember).isPresent();
        PartyMember findPartyMember = optionalPartyMember.get();

        Assertions.assertThat(findPartyMember)
                .hasFieldOrPropertyWithValue("partyId", partyMember.getPartyId())
                .hasFieldOrPropertyWithValue("memberId", partyMember.getMemberId())
                .hasFieldOrPropertyWithValue("role", partyMember.getRole());

    }

    @Test
    void 모임아이디로_모임원_조회_성공() {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        partyMemberJdbcRepository.save(partyMember);
        List<PartyMemberDTO> partyMemberDTOList = partyMemberJdbcRepository.findAllByPartyId(partyId);

        Assertions.assertThat(partyMemberDTOList).hasSize(1);
    }

    @Test
    void 사용자아이디와_모임아이디로_모임원_조회_성공() {

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        partyMemberJdbcRepository.save(partyMember);
        Optional<PartyMember> optionalPartyMember = partyMemberJdbcRepository.findByMemberIdAndPartyId(memberId, partyId);

        Assertions.assertThat(optionalPartyMember).isPresent();
        PartyMember findPartyMember = optionalPartyMember.get();

        Assertions.assertThat(findPartyMember)
                .hasFieldOrPropertyWithValue("partyId", partyMember.getPartyId())
                .hasFieldOrPropertyWithValue("memberId", partyMember.getMemberId())
                .hasFieldOrPropertyWithValue("role", partyMember.getRole());
    }

    @Test
    void 모임원_삭제_성공() {
        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        Long id = partyMemberJdbcRepository.save(partyMember);
        partyMemberJdbcRepository.delete(id);

        Optional<PartyMember> optionalPartyMember = partyMemberJdbcRepository.findById(id);
        Assertions.assertThat(optionalPartyMember).isEmpty();
    }


}