package com.geonwoo.assemble.domain.pricecomment.model;

import lombok.Getter;

@Getter
public class PriceComment {

    private Long id;
    private Long priceId;
    private Long partyMemberId;
    private String comment;

    public PriceComment(Long priceId, Long partyMemberId, String comment) {
        this.priceId = priceId;
        this.partyMemberId = partyMemberId;
        this.comment = comment;
    }
}
