package com.geonwoo.assemble.domain.party.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Party {

    private Long id;
    private String name;
    private String content;
    private LocalDate start_date;

    public Party(String name, String content, LocalDate start_date) {
        this.name = name;
        this.content = content;
        this.start_date = start_date;
    }
}
