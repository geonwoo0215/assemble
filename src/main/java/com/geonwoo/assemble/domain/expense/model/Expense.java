package com.geonwoo.assemble.domain.expense.model;

import com.geonwoo.assemble.domain.expense.dto.ExpenseDTO;
import com.geonwoo.assemble.domain.expense.dto.ExpenseDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Expense {

    private Long id;
    private Long partyId;
    private Integer price;
    private String content;

    public Expense(Long partyId, Integer price, String content) {
        this.partyId = partyId;
        this.price = price;
        this.content = content;
    }

    public ExpenseDetailDTO toExpenseDetailDTO(Integer payerPrice, Integer restPrice, List<String> memberNames) {
        return new ExpenseDetailDTO(id, partyId, price, content, payerPrice, restPrice, memberNames);
    }

    public ExpenseDTO toExpenseDTO() {
        return new ExpenseDTO(id, partyId, price, content);
    }
}

