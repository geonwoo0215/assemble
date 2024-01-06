package com.geonwoo.assemble.domain.invitation.service;

import com.geonwoo.assemble.domain.invitation.exception.InvitationNotFoundException;
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

        return invitationJdbcRepository.findByPartyId(partyId)
                .filter(invitation -> !invitation.isExpired())
                .map(Invitation::getInviteCode)
                .orElseGet(() -> {
                    Invitation newInvitation = new Invitation(partyId, LocalDateTime.now().plusHours(2L));
                    invitationJdbcRepository.save(newInvitation);
                    return newInvitation.getInviteCode();
                });
    }

    public Long validateCode(String inviteCode) {
        Invitation invitation = invitationJdbcRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new InvitationNotFoundException(inviteCode));
        invitation.validateExpiredDate();
        return invitation.getPartyId();
    }

}
