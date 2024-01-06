package com.geonwoo.assemble.domain.expensecomment.service;

import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentDTO;
import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentSaveDTO;
import com.geonwoo.assemble.domain.expensecomment.exception.ExpenseCommentNotFoundException;
import com.geonwoo.assemble.domain.expensecomment.model.ExpenseComment;
import com.geonwoo.assemble.domain.expensecomment.repository.ExpenseCommentJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseCommentService {

    private final ExpenseCommentJdbcRepository expenseCommentJdbcRepository;

    @Transactional
    public Long save(Long expenseId, ExpenseCommentSaveDTO expenseCommentSaveDTO) {

        Long maxGroupNo = expenseCommentJdbcRepository.findMaxGroupNoByExpenseId(expenseId);

        Long id;

        if (expenseCommentSaveDTO.getCommentId() == 0) {
            ExpenseComment expenseComment = new ExpenseComment(expenseId,
                    expenseCommentSaveDTO.getPartyMemberId(),
                    expenseCommentSaveDTO.getComment(),
                    maxGroupNo + 1,
                    0L,
                    0L,
                    0L);

            id = expenseCommentJdbcRepository.save(expenseComment);
        } else {

            ExpenseComment parentExpenseComment = expenseCommentJdbcRepository.findById(expenseCommentSaveDTO.getCommentId())
                    .orElseThrow(() -> new ExpenseCommentNotFoundException(expenseCommentSaveDTO.getCommentId()));

            ExpenseComment expenseComment = new ExpenseComment(expenseId,
                    expenseCommentSaveDTO.getPartyMemberId(),
                    expenseCommentSaveDTO.getComment(),
                    parentExpenseComment.getGroupNo(),
                    parentExpenseComment.getDepth() + 1,
                    getCommentOrder(parentExpenseComment) + 1,
                    expenseCommentSaveDTO.getCommentId());

            id = expenseCommentJdbcRepository.save(expenseComment);
        }

        return id;
    }

    private Long getCommentOrder(ExpenseComment parentExpenseComment) {
        Long maxCommentOrder = expenseCommentJdbcRepository.findMaxCommentOrderById(parentExpenseComment.getId());
        if (maxCommentOrder == 0) {
            maxCommentOrder = parentExpenseComment.getCommentOrder();
        }
        expenseCommentJdbcRepository.updateCommentOrder(parentExpenseComment.getExpenseId(), parentExpenseComment.getGroupNo(), maxCommentOrder);
        return maxCommentOrder;
    }

    public List<ExpenseCommentDTO> findAllByExpenseId(Long expenseId) {

        return expenseCommentJdbcRepository.findAllByExpenseId(expenseId)
                .stream()
                .map(ExpenseComment::toExpenseCommentDTO)
                .collect(Collectors.toList());
    }

}
