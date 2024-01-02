package com.geonwoo.assemble.domain.expense.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ExpenseDetailDTO {

    private Long id;
    private String payerName;
    private Integer price;
    private String content;
    private Integer individualPrice;
    private List<String> memberNames;
    private List<String> imageUrls;

    public ExpenseDetailDTO(Long id, String payerName, Integer price, String content, Integer individualPrice, List<String> memberNames, List<String> imageUrls) {
        this.id = id;
        this.payerName = payerName;
        this.price = price;
        this.content = content;
        this.individualPrice = individualPrice;
        this.memberNames = memberNames;
        this.imageUrls = imageUrls;
    }
}
