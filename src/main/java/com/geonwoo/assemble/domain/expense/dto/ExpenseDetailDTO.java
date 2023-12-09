package com.geonwoo.assemble.domain.expense.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ExpenseDetailDTO {

    private Long id;
    private Long partyId;
    private Integer price;
    private String content;
    private Integer payerPrice;
    private Integer restPrice;
    private List<String> memberNames;

    public ExpenseDetailDTO(Long id, Long partyId, Integer price, String content, Integer payerPrice, Integer restPrice, List<String> memberNames) {
        this.id = id;
        this.partyId = partyId;
        this.price = price;
        this.content = content;
        this.payerPrice = payerPrice;
        this.restPrice = restPrice;
        this.memberNames = memberNames;
    }
}
