package com.geonwoo.assemble.domain.party.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartyUpdateDTO {

    private String name;
    private String content;
    private LocalDate startDate;

    public PartyUpdateDTO(String name, String content, LocalDate startDate) {
        this.name = name;
        this.content = content;
        this.startDate = startDate;
    }
}
