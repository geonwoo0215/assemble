package com.geonwoo.assemble.domain.invitation.service;

import com.geonwoo.assemble.domain.invitation.model.Invitation;
import com.geonwoo.assemble.domain.invitation.repository.InvitationJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationJdbcRepository invitationJdbcRepository;


    @Transactional
    public String save(Long partyId) {
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2L));
        invitationJdbcRepository.save(invitation);
        return invitation.getInviteCode();
    }

    public Long validateCode(String inviteCode) {
        Invitation invitation = invitationJdbcRepository.findByInviteCode(inviteCode)
                .orElseThrow(RuntimeException::new);
        invitation.validateInviteCode();
        return invitation.getPartyId();
    }

}
