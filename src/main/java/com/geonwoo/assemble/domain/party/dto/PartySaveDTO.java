package com.geonwoo.assemble.domain.party.dto;

import com.geonwoo.assemble.domain.party.model.Party;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartySaveDTO {

    @NotBlank(message = "모임 제목을 입력해주세요.")
    private String name;

    @NotBlank(message = "모임 내용을 입력해주세요.")
    private String content;

    @FutureOrPresent(message = "모임 날짜를 현재이거나 이후로 입력해주세요.")
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
