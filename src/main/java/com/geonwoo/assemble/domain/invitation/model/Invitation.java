package com.geonwoo.assemble.domain.invitation.model;

import com.geonwoo.assemble.domain.invitation.model.algorithm.Sha256Algorithm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {

    private Long id;
    private Long partyId;
    private LocalDateTime expiredDate;
    private String inviteCode;

    public Invitation(Long partyId, LocalDateTime expiredDate) {
        this.partyId = partyId;
        this.expiredDate = expiredDate;
        createInviteCode(partyId + UUID.randomUUID().toString());
    }

    private void createInviteCode(String value) {
        this.inviteCode = Sha256Algorithm.execute(value);
    }

    public void validateExpiredDate() {
        if (expiredDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException();
        }
    }

    public boolean isExpired() {
        if (expiredDate.isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
}
