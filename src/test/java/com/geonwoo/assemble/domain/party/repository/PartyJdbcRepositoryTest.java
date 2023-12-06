package com.geonwoo.assemble.domain.party.repository;

import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
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
import java.util.Optional;

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

    @Test
    void findById() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = repository.save(party);

        Optional<Party> optionalParty = repository.findById(id);
        Assertions.assertThat(optionalParty).isPresent();
        Party saveParty = optionalParty.get();
        Assertions.assertThat(saveParty)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", party.getName())
                .hasFieldOrPropertyWithValue("content", party.getContent())
                .hasFieldOrPropertyWithValue("startDate", party.getStartDate());

    }

    @Test
    void update() {
        Party party = new Party("name", "content", LocalDate.now());
        Long id = repository.save(party);

        PartyUpdateDTO partyUpdateDTO = new PartyUpdateDTO("updateDTO", "updateContent", LocalDate.now().minusDays(1L));
        repository.update(id, partyUpdateDTO);

        Optional<Party> optionalParty = repository.findById(id);
        Assertions.assertThat(optionalParty).isPresent();
        Party saveParty = optionalParty.get();
        Assertions.assertThat(saveParty)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", partyUpdateDTO.getName())
                .hasFieldOrPropertyWithValue("content", partyUpdateDTO.getContent())
                .hasFieldOrPropertyWithValue("startDate", partyUpdateDTO.getStartDate());
    }
}