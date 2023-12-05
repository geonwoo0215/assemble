package com.geonwoo.assemble.domain.member.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

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
        Assertions.assertThat(id).isNotNull();
    }

}