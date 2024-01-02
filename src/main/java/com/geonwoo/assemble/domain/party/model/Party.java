package com.geonwoo.assemble.domain.party.model;

import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.dto.PartyDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    public PartyDetailDTO toPartyDetailDTO(List<String> imageUrls) {
        return new PartyDetailDTO(id, name, content, eventDate, imageUrls);
    }
}
