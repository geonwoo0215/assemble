package com.geonwoo.assemble.domain.expense.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDate;

@JdbcTest
@Sql("classpath:schema.sql")
class ExpenseJdbcRepositoryTest {
    MemberJdbcRepository memberJdbcRepository;
    PartyJdbcRepository partyJdbcRepository;
    ExpenseJdbcRepository expenseJdbcRepository;

    @Autowired
    DataSource dataSource;

    Long memberId;
    Long partyId;

    @BeforeEach
    void setUp() {
        memberJdbcRepository = new MemberJdbcRepository(dataSource);
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        expenseJdbcRepository = new ExpenseJdbcRepository(dataSource);
        Member member = new Member("loginId", "password", "email");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);
    }

    @Test
    void save() {

        Expense expense = new Expense(partyId, 1000, "1차 비용");
        Long id = expenseJdbcRepository.save(expense);
        Assertions.assertThat(id).isNotNull();
    }
}