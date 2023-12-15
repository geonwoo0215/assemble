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
    public Long save(Long userId, Long partyId) {
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2L));
        Long id = invitationJdbcRepository.save(invitation);
        return id;
    }
    
}
