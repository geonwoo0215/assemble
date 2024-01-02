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

    public ExpenseDetailDTO toExpenseDetailDTO(Integer individualPrice, String payerName, List<String> memberNames, List<String> imageUrls) {
        return new ExpenseDetailDTO(id, payerName, price, content, individualPrice, memberNames, imageUrls);
    }

    public ExpenseDTO toExpenseDTO() {
        return new ExpenseDTO(id, partyId, price, content);
    }
}

