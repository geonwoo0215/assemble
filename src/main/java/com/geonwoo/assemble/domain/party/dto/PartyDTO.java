package com.geonwoo.assemble.domain.party.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartyDTO {

    private Long id;
    private String name;
    private String content;
    private LocalDate startDate;

    public PartyDTO(Long id, String name, String content, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.startDate = startDate;
    }

}
