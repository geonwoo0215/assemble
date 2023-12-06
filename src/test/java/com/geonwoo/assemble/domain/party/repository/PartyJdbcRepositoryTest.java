package com.geonwoo.assemble.domain.party.repository;

import com.geonwoo.assemble.domain.party.model.Party;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDate;

@JdbcTest
@Sql("classpath:schema.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PartyJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    PartyJdbcRepository repository;

    @BeforeAll
    void setUp() {
        repository = new PartyJdbcRepository(dataSource);
    }

    @Test
    void save() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = repository.save(party);
        Assertions.assertThat(id).isNotNull();
    }

}