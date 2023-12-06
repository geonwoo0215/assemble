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
    private LocalDate startDate;

    public Party(String name, String content, LocalDate startDate) {
        this.name = name;
        this.content = content;
        this.startDate = startDate;
    }

    public PartyDTO toPartyDTO() {
        return new PartyDTO(id, name, content, startDate);
    }
}
