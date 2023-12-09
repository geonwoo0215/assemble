package com.geonwoo.assemble.domain.expense.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExpenseDTO {

    private Long id;
    private Long partyId;
    private Integer price;
    private String content;

    public ExpenseDTO(Long id, Long partyId, Integer price, String content) {
        this.id = id;
        this.partyId = partyId;
        this.price = price;
        this.content = content;
    }
}
