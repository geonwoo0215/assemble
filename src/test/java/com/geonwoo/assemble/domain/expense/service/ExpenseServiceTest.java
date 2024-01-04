package com.geonwoo.assemble.domain.expense.service;

import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.imageurl.model.ImageUrl;
import com.geonwoo.assemble.domain.imageurl.repository.ImageUrlJdbcRepository;
import com.geonwoo.assemble.domain.partymemberexpense.dto.PartyMemberExpenseDTO;
import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
import com.geonwoo.assemble.domain.partymemberexpense.repository.PartyMemberExpenseJdbcRepository;
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
class ExpenseServiceTest {

    @InjectMocks
    ExpenseService expenseService;

    @Mock
    ExpenseJdbcRepository expenseJdbcRepository;

    @Mock
    PartyMemberExpenseJdbcRepository partyMemberExpenseJdbcRepository;

    @Mock
    ImageUrlJdbcRepository imageUrlRepository;

    @Test
    void 비용_저장_성공() {

        ExpenseSaveDTO expenseSaveDTO = new ExpenseSaveDTO(1L, 1000, "content", List.of(1L), List.of("url"));

        Mockito.when(expenseJdbcRepository.save(Mockito.any(Expense.class))).thenReturn(1L);
        Mockito.when(partyMemberExpenseJdbcRepository.save(Mockito.any(PartyMemberExpense.class))).thenReturn(1L);
        Mockito.when(imageUrlRepository.save(Mockito.any(ImageUrl.class))).thenReturn(1L);

        expenseService.save(1L, expenseSaveDTO);

        Mockito.verify(expenseJdbcRepository).save(Mockito.any(Expense.class));
        Mockito.verify(partyMemberExpenseJdbcRepository, Mockito.times(2)).save(Mockito.any(PartyMemberExpense.class));
        Mockito.verify(imageUrlRepository).save(Mockito.any(ImageUrl.class));

    }

    @Test
    void 비용아이디로_비용_정산인원_이미지_조회_성공() {

        Long expenseId = 1L;
        Long partyId = 1L;
        Expense expense = new Expense(partyId, 1000, "content");
        PartyMemberExpenseDTO partyMemberExpenseDTO = new PartyMemberExpenseDTO(true, "nickname");

        Mockito.when(expenseJdbcRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        Mockito.when(partyMemberExpenseJdbcRepository.findByExpenseId(expenseId)).thenReturn(List.of(partyMemberExpenseDTO));
        Mockito.when(imageUrlRepository.findByExpenseId(expenseId)).thenReturn(List.of("url"));

        expenseService.findExpenseAndMembersById(expenseId);

        Mockito.verify(expenseJdbcRepository).findById(expenseId);
        Mockito.verify(partyMemberExpenseJdbcRepository).findByExpenseId(expenseId);
        Mockito.verify(imageUrlRepository).findByExpenseId(expenseId);
    }

    @Test
    void 모임아이디로_비용_조회_성공() {

        Long partyId = 1L;
        Expense expense = new Expense(partyId, 1000, "content");

        Mockito.when(expenseJdbcRepository.findAllByPartyId(partyId)).thenReturn(List.of(expense));

        expenseService.findAllByPartyId(partyId);

        Mockito.verify(expenseJdbcRepository).findAllByPartyId(partyId);

    }

}