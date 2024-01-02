package com.geonwoo.assemble.domain.party.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PartyDetailDTO {

    private Long id;
    private String name;
    private String content;
    private LocalDate eventDate;
    private List<String> imageUrls;

    public PartyDetailDTO(Long id, String name, String content, LocalDate eventDate, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.eventDate = eventDate;
        this.imageUrls = imageUrls;
    }

}
