package com.geonwoo.assemble.domain.invitation.service;

import com.geonwoo.assemble.domain.invitation.model.Invitation;
import com.geonwoo.assemble.domain.invitation.repository.InvitationJdbcRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InvitationServiceTest {

    @Mock
    InvitationJdbcRepository invitationJdbcRepository;

    @InjectMocks
    InvitationService invitationService;

    @Test
    void 초대장_저장_성공() {
        Long partyId = 1L;
        Long invitationId = 1L;
        Mockito.when(invitationJdbcRepository.findByPartyId(partyId)).thenReturn(Optional.empty());
        Mockito.when(invitationJdbcRepository.save(Mockito.any(Invitation.class))).thenReturn(invitationId);

        invitationService.save(partyId);

        Mockito.verify(invitationJdbcRepository).findByPartyId(partyId);
        Mockito.verify(invitationJdbcRepository).save(Mockito.any(Invitation.class));
    }

    @Test
    void 저장된초대장이_만료됬을경우_초대장_저장_성공() {
        Long partyId = 1L;
        Long invitationId = 1L;
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().minusHours(2L));
        Mockito.when(invitationJdbcRepository.findByPartyId(partyId)).thenReturn(Optional.of(invitation));
        Mockito.when(invitationJdbcRepository.save(Mockito.any(Invitation.class))).thenReturn(invitationId);

        invitationService.save(partyId);

        Mockito.verify(invitationJdbcRepository).findByPartyId(partyId);
        Mockito.verify(invitationJdbcRepository).save(Mockito.any(Invitation.class));
    }


    @Test
    void 저장된초대장이있을경우_초대장_조회_성공() {
        Long partyId = 1L;
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2L));
        Mockito.when(invitationJdbcRepository.findByPartyId(partyId)).thenReturn(Optional.of(invitation));

        invitationService.save(partyId);

        Mockito.verify(invitationJdbcRepository).findByPartyId(partyId);
    }

    @Test
    void 초대장_검증_성공() {
        Long partyId = 1L;
        Invitation invitation = new Invitation(partyId, LocalDateTime.now().plusHours(2L));

        Mockito.when(invitationJdbcRepository.findByInviteCode(invitation.getInviteCode())).thenReturn(Optional.of(invitation));

        invitationService.validateCode(invitation.getInviteCode());

        Mockito.verify(invitationJdbcRepository).findByInviteCode(invitation.getInviteCode());
    }
}