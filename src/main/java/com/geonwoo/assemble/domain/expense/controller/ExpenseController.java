package com.geonwoo.assemble.domain.expense.controller;

import com.geonwoo.assemble.domain.expense.dto.ExpenseDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseDetailDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.service.ExpenseService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping(value = "/partys/{partyId}/expense", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody ExpenseSaveDTO expenseSaveDTO,
                    @PathVariable("partyId") Long partyId,
                    HttpServletRequest request
            ) {
        Long id = expenseService.save(partyId, expenseSaveDTO);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));

    }

    @GetMapping(value = "/partys/{partyId}/expense/{expenseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ExpenseDetailDTO>> findById(
            @PathVariable("expenseId") Long expenseId
    ) {
        ExpenseDetailDTO expenseDTO = expenseService.findExpenseAndMembersById(expenseId);

        return ResponseEntity.ok(new ApiResponse<>(expenseDTO));
    }

    @GetMapping(value = "/partys/{partyId}/expense", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> findAllByPartyId
            (
                    @PathVariable("partyId") Long partyId
            ) {
        List<ExpenseDTO> expenseDTOList = expenseService.findAllByPartyId(partyId);

        return ResponseEntity.ok(new ApiResponse<>(expenseDTOList));
    }

}
