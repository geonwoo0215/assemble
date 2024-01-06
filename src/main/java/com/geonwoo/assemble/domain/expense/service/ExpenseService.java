package com.geonwoo.assemble.domain.expense.service;

import com.geonwoo.assemble.domain.expense.dto.ExpenseDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseDetailDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.exception.ExpenseNotFoundException;
import com.geonwoo.assemble.domain.expense.exception.PayerNotFoundException;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.imageurl.model.ImageUrl;
import com.geonwoo.assemble.domain.imageurl.repository.ImageUrlJdbcRepository;
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
    private final ImageUrlJdbcRepository imageUrlRepository;

    @Transactional
    public Long save(Long partyId, ExpenseSaveDTO expenseSaveDTO) {

        Long payerPartyMemberId = expenseSaveDTO.getPayerPartyMemberId();
        List<Long> partyMemberIds = expenseSaveDTO.getPartyMemberIds();
        Expense expense = expenseSaveDTO.toExpense(partyId);
        Long expenseId = expenseJdbcRepository.save(expense);

        PartyMemberExpense payer = new PartyMemberExpense(expenseId, payerPartyMemberId, true);
        partyMemberExpenseJdbcRepository.save(payer);

        partyMemberIds.forEach(partyMemberId -> {
            PartyMemberExpense nonPayer = new PartyMemberExpense(expenseId, partyMemberId, false);
            partyMemberExpenseJdbcRepository.save(nonPayer);
        });

        expenseSaveDTO.getImageUrls().forEach(url -> {
            ImageUrl imageUrl = new ImageUrl(partyId, expenseId, url);
            imageUrlRepository.save(imageUrl);
        });

        return expenseId;
    }

    public ExpenseDetailDTO findExpenseAndMembersById(Long id) {
        Expense expense = expenseJdbcRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));
        List<PartyMemberExpenseDTO> memberExpenseDTOList = partyMemberExpenseJdbcRepository.findByExpenseId(id);

        String payerNickname = memberExpenseDTOList.stream()
                .filter(PartyMemberExpenseDTO::isPayer)
                .findFirst()
                .map(PartyMemberExpenseDTO::getNickname)
                .orElseThrow(PayerNotFoundException::new);

        List<String> memberNames = memberExpenseDTOList.stream()
                .filter(partyMemberExpenseDTO -> !partyMemberExpenseDTO.isPayer())
                .map(PartyMemberExpenseDTO::getNickname)
                .collect(Collectors.toList());

        Integer individualPrice = expense.getPrice() / memberExpenseDTOList.size();

        List<String> imageUrls = imageUrlRepository.findByExpenseId(id);

        ExpenseDetailDTO expenseDetailDTO = expense.toExpenseDetailDTO(individualPrice, payerNickname, memberNames, imageUrls);
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
