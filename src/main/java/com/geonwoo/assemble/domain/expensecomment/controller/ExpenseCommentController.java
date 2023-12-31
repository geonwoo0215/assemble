package com.geonwoo.assemble.domain.expensecomment.controller;

import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentDTO;
import com.geonwoo.assemble.domain.expensecomment.dto.ExpenseCommentSaveDTO;
import com.geonwoo.assemble.domain.expensecomment.service.ExpenseCommentService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExpenseCommentController {

    private final ExpenseCommentService expenseCommentService;

    @PostMapping(value = "/partys/{partyId}/expense/{expenseId}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody ExpenseCommentSaveDTO expenseCommentSaveDTO,
                    @PathVariable("expenseId") Long expenseId,
                    HttpServletRequest request
            ) {
        Long id = expenseCommentService.save(expenseId, expenseCommentSaveDTO);
        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/partys/{partyId}/expense/{expenseId}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<ExpenseCommentDTO>>> findAllByExpenseId
            (
                    @PathVariable("expenseId") Long expenseId
            ) {
        List<ExpenseCommentDTO> commentDTOList = expenseCommentService.findAllByExpenseId(expenseId);
        return ResponseEntity.ok(new ApiResponse<>(commentDTOList));
    }
}
