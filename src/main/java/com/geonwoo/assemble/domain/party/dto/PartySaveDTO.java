package com.geonwoo.assemble.domain.party.dto;

import com.geonwoo.assemble.domain.party.model.Party;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartySaveDTO {

    private String name;
    private String content;
    private LocalDate startDate;

    public PartySaveDTO(String name, String content, LocalDate startDate) {
        this.name = name;
        this.content = content;
        this.startDate = startDate;
    }

    public Party toParty() {
        return new Party(name, content, startDate);
    }
}
