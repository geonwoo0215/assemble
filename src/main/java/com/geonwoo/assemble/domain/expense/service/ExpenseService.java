package com.geonwoo.assemble.domain.expense.service;

import com.geonwoo.assemble.domain.expense.dto.ExpenseDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseDetailDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.dto.PartyMemberExpenseDTO;
import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
import com.geonwoo.assemble.domain.partymemberexpense.repository.PartyMemberExpenseJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseJdbcRepository expenseJdbcRepository;
    private final PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;

    @Transactional
    public Long save(Long partyId, ExpenseSaveDTO expenseSaveDTO) {

        Long payerPartyMemberId = expenseSaveDTO.getPayerPartyMemberId();
        List<Long> partyMemberIds = expenseSaveDTO.getPartyMemberIds();
        Expense expense = expenseSaveDTO.toExpense(partyId);
        Long expenseId = expenseJdbcRepository.save(expense);

        PartyMemberExpense payer = new PartyMemberExpense(partyId, payerPartyMemberId, true);
        partyMemberExpenseJdbcRepository.save(payer);

        partyMemberIds.forEach(partyMemberId -> {
            PartyMemberExpense nonPayer = new PartyMemberExpense(partyId, partyMemberId, false);
            partyMemberExpenseJdbcRepository.save(nonPayer);
        });

        return expenseId;
    }

    public ExpenseDetailDTO findExpenseAndMembersById(Long id) {
        Expense expense = expenseJdbcRepository.findById(id).orElseThrow(RuntimeException::new);
        List<PartyMemberExpenseDTO> memberExpenseDTOList = partyMemberExpenseJdbcRepository.findByExpenseId(expense.getId());

        String payerNickname = memberExpenseDTOList.stream()
                .filter(PartyMemberExpenseDTO::isPayer)
                .findFirst()
                .map(PartyMemberExpenseDTO::getNickname)
                .orElseThrow(RuntimeException::new);
        List<String> memberNames = memberExpenseDTOList.stream()
                .filter(partyMemberExpenseDTO -> !partyMemberExpenseDTO.isPayer())
                .map(PartyMemberExpenseDTO::getNickname)
                .collect(Collectors.toList());

        Integer individualPrice = expense.getPrice() / memberExpenseDTOList.size();

        ExpenseDetailDTO expenseDetailDTO = expense.toExpenseDetailDTO(individualPrice, payerNickname, memberNames);
        return expenseDetailDTO;
    }

    public List<ExpenseDTO> findAllByPartyId(Long partyId) {

        List<ExpenseDTO> expenseDTOList = expenseJdbcRepository.findAllByPartyId(partyId)
                .stream()
                .map(Expense::toExpenseDTO)
                .toList();

        return expenseDTOList;
    }


}
