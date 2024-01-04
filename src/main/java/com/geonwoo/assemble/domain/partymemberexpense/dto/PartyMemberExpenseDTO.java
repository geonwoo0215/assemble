package com.geonwoo.assemble.domain.partymemberexpense.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartyMemberExpenseDTO {

    private boolean payer;
    private String nickname;

    public PartyMemberExpenseDTO(boolean payer, String nickname) {
        this.payer = payer;
        this.nickname = nickname;
    }
}
