package com.geonwoo.assemble.domain.member.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:schema.sql")
class MemberJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;
    MemberJdbcRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemberJdbcRepository(dataSource);
    }

    @Test
    void 사용자_저장_성공() {
        Member member = new Member("loginId", "password", "email", "nickname");
        Long id = repository.save(member);
        assertThat(id).isNotNull();
    }

    @Test
    void 사용자아이디로_조회_성공() {
        Member member = new Member("loginId", "password", "email", "nickname");
        Long id = repository.save(member);

        Optional<Member> optionalMember = repository.findByLoginId(member.getLoginId());
        assertThat(optionalMember).isPresent();
        Member saveMember = optionalMember.get();
        assertThat(saveMember)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("loginId", member.getLoginId())
                .hasFieldOrPropertyWithValue("password", member.getPassword())
                .hasFieldOrPropertyWithValue("email", member.getEmail())
                .hasFieldOrPropertyWithValue("nickname", member.getNickname())
                .hasFieldOrPropertyWithValue("role", member.getRole());

    }

}