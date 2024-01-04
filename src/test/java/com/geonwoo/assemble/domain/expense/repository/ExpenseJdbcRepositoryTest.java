package com.geonwoo.assemble.domain.expense.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
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
import java.util.stream.IntStream;

@JdbcTest
@Sql("classpath:schema.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);
    }

    @Test
    void 비용_저장_성공() {
        Expense expense = new Expense(partyId, 1000, "1차 비용");
        Long id = expenseJdbcRepository.save(expense);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 비용아이디로_비용_단건조회_성공() {
        Expense expense = new Expense(partyId, 1000, "1차 비용");
        Long id = expenseJdbcRepository.save(expense);

        Optional<Expense> optionalExpense = expenseJdbcRepository.findById(id);

        Assertions.assertThat(optionalExpense).isPresent();
        Expense saveExpense = optionalExpense.get();

        Assertions.assertThat(saveExpense)
                .hasFieldOrPropertyWithValue("partyId", expense.getPartyId())
                .hasFieldOrPropertyWithValue("price", expense.getPrice())
                .hasFieldOrPropertyWithValue("content", expense.getContent());
    }

    @Test
    void 비용_전체조회_성공() {


        List<Expense> expenses = IntStream.range(0, 20)
                .mapToObj(i -> {
                    Expense expense = new Expense(partyId, 1000 + i, i + "차 비용");
                    expenseJdbcRepository.save(expense);
                    return expense;
                })
                .toList();


        List<Expense> expenseList = expenseJdbcRepository.findAllByPartyId(partyId);

        Assertions.assertThat(expenseList).hasSize(expenses.size());
    }
}