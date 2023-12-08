package com.geonwoo.assemble.domain.expense.controller;

import com.geonwoo.assemble.domain.expense.dto.ExpenseSaveDTO;
import com.geonwoo.assemble.domain.expense.service.ExpenseService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping(value = "/partys/{partyId}/expense", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody ExpenseSaveDTO expenseSaveDTO,
                    HttpServletRequest request
            ) {
        Long id = expenseService.save(expenseSaveDTO);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));

    }


}
