package com.geonwoo.assemble.domain.party.model;

import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Party {

    private Long id;
    private String name;
    private String content;
    private LocalDate eventDate;

    public Party(String name, String content, LocalDate eventDate) {
        this.name = name;
        this.content = content;
        this.eventDate = eventDate;
    }

    public PartyDTO toPartyDTO() {
        return new PartyDTO(id, name, content, eventDate);
    }
}
