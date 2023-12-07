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
    void save() {
        Member member = new Member("loginId", "password", "email");
        Long id = repository.save(member);
        assertThat(id).isNotNull();
    }

    @Test
    void findByLoginId() {
        Member member = new Member("loginId", "password", "email");
        Long id = repository.save(member);

        Optional<Member> optionalMember = repository.findByLoginId(member.getLoginId());
        assertThat(optionalMember).isPresent();
        Member saveMember = optionalMember.get();
        assertThat(saveMember)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("loginId", member.getLoginId())
                .hasFieldOrPropertyWithValue("password", member.getPassword())
                .hasFieldOrPropertyWithValue("email", member.getEmail())
                .hasFieldOrPropertyWithValue("role", member.getRole());

    }

}