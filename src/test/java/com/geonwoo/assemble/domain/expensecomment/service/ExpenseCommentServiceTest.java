package com.geonwoo.assemble.domain.expensecomment.service;

import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentSaveDTO;
import com.geonwoo.assemble.domain.expensecomment.model.ExpenseComment;
import com.geonwoo.assemble.domain.expensecomment.repository.ExpenseCommentJdbcRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExpenseCommentServiceTest {

    @Mock
    ExpenseCommentJdbcRepository expenseCommentJdbcRepository;

    @InjectMocks
    ExpenseCommentService expenseCommentService;


    @Test
    void 댓글_저장_성공() {

        Long maxGroupNo = 0L;
        Long expenseId = 0L;
        Long expenseCommentId = 0L;
        Long commentId = 0L;
        Long partyMemberId = 0L;
        String comment = "comment";

        ExpenseCommentSaveDTO expenseCommentSaveDTO = new ExpenseCommentSaveDTO(commentId, partyMemberId, comment);

        Mockito.when(expenseCommentJdbcRepository.findMaxGroupNoByExpenseId(expenseId)).thenReturn(maxGroupNo);
        Mockito.when(expenseCommentJdbcRepository.save(Mockito.any(ExpenseComment.class))).thenReturn(expenseCommentId);

        expenseCommentService.save(expenseId, expenseCommentSaveDTO);

        Mockito.verify(expenseCommentJdbcRepository).findMaxGroupNoByExpenseId(expenseId);
        Mockito.verify(expenseCommentJdbcRepository).save(Mockito.any(ExpenseComment.class));

    }

    @Test
    void 대댓글_저장_성공() {

        Long maxGroupNo = 0L;
        Long expenseId = 0L;
        Long expenseCommentId = 0L;
        Long commentId = 1L;
        Long partyMemberId = 0L;
        String comment = "comment";
        Long depth = 0L;
        Long commentOrder = 0L;
        ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, comment, maxGroupNo, depth, commentOrder, 0L);
        ExpenseCommentSaveDTO expenseCommentSaveDTO = new ExpenseCommentSaveDTO(commentId, partyMemberId, comment);

        Mockito.when(expenseCommentJdbcRepository.findMaxGroupNoByExpenseId(expenseId)).thenReturn(maxGroupNo);
        Mockito.when(expenseCommentJdbcRepository.findById(commentId)).thenReturn(Optional.of(expenseComment));
        Mockito.when(expenseCommentJdbcRepository.save(Mockito.any(ExpenseComment.class))).thenReturn(expenseCommentId);
        Mockito.when(expenseCommentJdbcRepository.findMaxGroupNoByExpenseId(expenseId)).thenReturn(commentOrder);
        Mockito.doNothing().when(expenseCommentJdbcRepository).updateCommentOrder(expenseId, maxGroupNo, commentOrder);

        expenseCommentService.save(expenseId, expenseCommentSaveDTO);

        Mockito.verify(expenseCommentJdbcRepository).findMaxGroupNoByExpenseId(expenseId);
        Mockito.verify(expenseCommentJdbcRepository).findById(commentId);
        Mockito.verify(expenseCommentJdbcRepository).save(Mockito.any(ExpenseComment.class));
        Mockito.verify(expenseCommentJdbcRepository).findMaxGroupNoByExpenseId(expenseId);
        Mockito.verify(expenseCommentJdbcRepository).updateCommentOrder(expenseId, maxGroupNo, commentOrder);

    }

    @Test
    void 비용아이디로_댓글_조회_성공() {

        Long maxGroupNo = 0L;
        Long expenseId = 0L;
        Long partyMemberId = 0L;
        String comment = "comment";
        Long depth = 0L;
        Long commentOrder = 0L;

        ExpenseComment expenseComment = new ExpenseComment(expenseId, partyMemberId, comment, maxGroupNo, depth, commentOrder, 0L);
        Mockito.when(expenseCommentJdbcRepository.findAllByExpenseId(expenseId)).thenReturn(List.of(expenseComment));

        expenseCommentService.findAllByExpenseId(expenseId);

        Mockito.verify(expenseCommentJdbcRepository).findAllByExpenseId(expenseId);

    }

}