package com.geonwoo.assemble.domain.imageurl.model;

import lombok.Getter;

@Getter
public class ImageUrl {

    private Long id;
    private Long partyId;
    private Long expenseId;
    private String imgUrl;

    public ImageUrl(Long partyId, Long expenseId, String imgUrl) {
        this.partyId = partyId;
        this.expenseId = expenseId;
        this.imgUrl = imgUrl;
    }
}
