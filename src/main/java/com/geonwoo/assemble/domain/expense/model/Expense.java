package com.geonwoo.assemble.domain.expense.model;

import lombok.Getter;

@Getter
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
}
