package com.geonwoo.assemble.domain.invitation.model;

import com.geonwoo.assemble.domain.invitation.model.algorithm.Sha256Algorithm;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Invitation {

    private Long id;
    private Long partyId;
    private LocalDateTime expired_date;
    private String inviteCode;

    public Invitation(Long partyId, LocalDateTime expired_date) {
        this.partyId = partyId;
        this.expired_date = expired_date;
        createInviteCode(partyId + UUID.randomUUID().toString());
    }

    private void createInviteCode(String value) {
        this.inviteCode = Sha256Algorithm.execute(value);
    }

    public void validateInviteCode() {
        if (expired_date.isBefore(LocalDateTime.now())) {
            throw new RuntimeException();
        }
    }
}
