package com.geonwoo.assemble.domain.expensecomment.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.expensecomment.model.ExpenseComment;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import com.geonwoo.assemble.domain.partymember.model.PartyMemberRole;
import com.geonwoo.assemble.domain.partymember.repository.PartyMemberJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.repository.PartyMemberExpenseJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class ExpenseCommentJdbcRepositoryTest {

    MemberJdbcRepository memberJdbcRepository;
    PartyJdbcRepository partyJdbcRepository;
    PartyMemberJdbcRepository partyMemberJdbcRepository;
    PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;
    ExpenseJdbcRepository expenseJdbcRepository;
    ExpenseCommentJdbcRepository expenseCommentJdbcRepository;

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
        expenseCommentJdbcRepository = new ExpenseCommentJdbcRepository(dataSource);

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
    void 댓글_저장_성공() {
        ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, "comment", 0L, 0L, 0L, 0L);
        Long id = expenseCommentJdbcRepository.save(expenseComment);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 비용아이디로_댓글_조회_성공() {

        List<ExpenseComment> comments = IntStream.range(0, 10)
                .mapToObj(i -> {
                    ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, "comment", (long) i, 0L, 0L, 0L);
                    expenseCommentJdbcRepository.save(expenseComment);
                    return expenseComment;
                })
                .toList();

        List<ExpenseComment> commentList = expenseCommentJdbcRepository.findAllByExpenseId(expenseId);

        Assertions.assertThat(commentList).hasSize(comments.size());
    }

    @Test
    void 댓글아이디로_댓글_조회_성공() {

        ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, "comment", 0L, 0L, 0L, 0L);
        Long id = expenseCommentJdbcRepository.save(expenseComment);
        Optional<ExpenseComment> optionalExpenseComment = expenseCommentJdbcRepository.findById(id);

        Assertions.assertThat(optionalExpenseComment).isPresent();
        ExpenseComment comment = optionalExpenseComment.get();

        Assertions.assertThat(comment)
                .hasFieldOrPropertyWithValue("expenseId", expenseComment.getExpenseId())
                .hasFieldOrPropertyWithValue("partyMemberId", expenseComment.getPartyMemberId())
                .hasFieldOrPropertyWithValue("comment", expenseComment.getComment())
                .hasFieldOrPropertyWithValue("groupNo", expenseComment.getGroupNo())
                .hasFieldOrPropertyWithValue("depth", expenseComment.getDepth())
                .hasFieldOrPropertyWithValue("commentOrder", expenseComment.getCommentOrder())
                .hasFieldOrPropertyWithValue("parentId", expenseComment.getParentId());
    }

    @Test
    void 비용아이디로_최대그룹번호_조회_성공() {

        IntStream.range(0, 10)
                .forEach(i -> {
                    ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, "comment", (long) i, 0L, (long) i, 0L);
                    expenseCommentJdbcRepository.save(expenseComment);
                });

        Long maxGroupNo = expenseCommentJdbcRepository.findMaxGroupNoByExpenseId(expenseId);

        Assertions.assertThat(maxGroupNo).isEqualTo(9);

    }

    @Test
    void 부모댓글아이디로_최대댓글순서_조회_성공() {

        ExpenseComment expenseComment1 = new ExpenseComment(expenseId, partyMemberId, "comment", 0L, 0L, 0L, 0L);
        Long expenseCommentId = expenseCommentJdbcRepository.save(expenseComment1);

        IntStream.range(1, 10)
                .forEach(i -> {
                    ExpenseComment expenseComment2 = new ExpenseComment(expenseId, partyMemberId, "comment", 0L, 1L, (long) i, expenseCommentId);
                    expenseCommentJdbcRepository.save(expenseComment2);
                });

        Long maxCommentOrder = expenseCommentJdbcRepository.findMaxCommentOrderById(expenseCommentId);

        Assertions.assertThat(maxCommentOrder).isEqualTo(9);
    }

    @Test
    void 댓글순서_수정_성공() {

        Long groupNo = 0L;

        Long commentOrder = 0L;

        ExpenseComment expenseComment1 = new ExpenseComment(expenseId, partyMemberId, "comment", groupNo, 0L, commentOrder, 0L);
        Long expenseCommentId1 = expenseCommentJdbcRepository.save(expenseComment1);

        ExpenseComment expenseComment2 = new ExpenseComment(expenseId, partyMemberId, "comment", groupNo, 1L, 1L, expenseCommentId1);
        Long expenseCommentId12 = expenseCommentJdbcRepository.save(expenseComment2);

        expenseCommentJdbcRepository.updateCommentOrder(expenseId, groupNo, commentOrder);

        Optional<ExpenseComment> optionalExpenseComment = expenseCommentJdbcRepository.findById(expenseCommentId12);

        Assertions.assertThat(optionalExpenseComment).isPresent();
        ExpenseComment expenseComment = optionalExpenseComment.get();

        Assertions.assertThat(expenseComment.getCommentOrder()).isEqualTo(expenseComment2.getCommentOrder() + 1);

    }
}