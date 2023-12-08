package com.geonwoo.assemble.domain.expense.service;

import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
import com.geonwoo.assemble.domain.partymemberexpense.repository.PartyMemberExpenseJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseJdbcRepository expenseJdbcRepository;
    private final PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;

    @Transactional
    public Long save(ExpenseSaveDTO expenseSaveDTO) {

        Long partyId = expenseSaveDTO.getPartyId();
        Long payerPartyMemberId = expenseSaveDTO.getPayerPartyMemberId();
        List<Long> partyMemberIds = expenseSaveDTO.getPartyMemberIds();
        Expense expense = expenseSaveDTO.toExpense();
        Long expenseId = expenseJdbcRepository.save(expense);

        PartyMemberExpense payer = new PartyMemberExpense(partyId, payerPartyMemberId, true);
        partyMemberExpenseJdbcRepository.save(payer);

        partyMemberIds.forEach(partyMemberId -> {
            PartyMemberExpense nonPayer = new PartyMemberExpense(partyId, partyMemberId, false);
            partyMemberExpenseJdbcRepository.save(nonPayer);
        });

        return expenseId;
    }


}
