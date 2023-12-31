package com.geonwoo.assemble.domain.party.dto;

import com.geonwoo.assemble.domain.party.model.Party;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartySaveDTO {

    private String name;
    private String content;
    private LocalDate eventDate;

    public PartySaveDTO(String name, String content, LocalDate eventDate) {
        this.name = name;
        this.content = content;
        this.eventDate = eventDate;
    }

    public Party toParty() {
        return new Party(name, content, eventDate);
    }
}
