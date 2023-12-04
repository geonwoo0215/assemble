package com.geonwoo.assemble.domain.price.model;

import lombok.Getter;

@Getter
public class Price {

    private Long id;
    private Long partyId;
    private Integer value;
    private String content;

    public Price(Long partyId, Integer value, String content) {
        this.partyId = partyId;
        this.value = value;
        this.content = content;
    }
}
