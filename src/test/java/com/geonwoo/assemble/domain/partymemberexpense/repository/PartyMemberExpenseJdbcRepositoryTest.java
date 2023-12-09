package com.geonwoo.assemble.domain.partymemberexpense.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
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
class PartyMemberExpenseJdbcRepositoryTest {

    MemberJdbcRepository memberJdbcRepository;
    PartyJdbcRepository partyJdbcRepository;
    PartyMemberJdbcRepository partyMemberJdbcRepository;
    PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;
    ExpenseJdbcRepository expenseJdbcRepository;
    @Autowired
    DataSource dataSource;

    Long memberId;
    Long partyId;
    Long partyMemberId;
    Long expenseId;

    @BeforeEach
    void setUp() {
        memberJdbcRepository = new MemberJdbcRepository(dataSource);
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        partyMemberJdbcRepository = new PartyMemberJdbcRepository(dataSource);
        expenseJdbcRepository = new ExpenseJdbcRepository(dataSource);
        partyMemberExpenseJdbcRepository = new PartyMemberExpenseJdbcRepository(dataSource);

        Member member = new Member("loginId", "password", "email", "nickname");
        memberId = memberJdbcRepository.save(member);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        PartyMember partyMember = new PartyMember(partyId, memberId, PartyMemberRole.MEMBER);
        partyMemberId = partyMemberJdbcRepository.save(partyMember);

        Expense expense = new Expense(partyId, 1000, "1차 비용");
        expenseId = expenseJdbcRepository.save(expense);

    }

    @Test
    void save() {

        PartyMemberExpense partyMemberExpense = new PartyMemberExpense(expenseId, partyMemberId, true);
        Long id = partyMemberExpenseJdbcRepository.save(partyMemberExpense);
        Assertions.assertThat(id).isNotNull();
    }

}