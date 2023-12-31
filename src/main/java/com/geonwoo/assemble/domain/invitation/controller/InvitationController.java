package com.geonwoo.assemble.domain.invitation.controller;

import com.geonwoo.assemble.domain.invitation.service.InvitationService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping(value = "/partys/{partyId}/invitation")
    public ResponseEntity<ApiResponse<String>> save
            (
                    @PathVariable("partyId") Long partyId
            ) {
        String inviteCode = invitationService.save(partyId);
        return new ResponseEntity<>(new ApiResponse<>(inviteCode), HttpStatus.CREATED);
    }

    @GetMapping(value = "/invite/{inviteCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> validateInviteCode
            (
                    @PathVariable("inviteCode") String inviteCode
            ) {
        Long partyId = invitationService.validateCode(inviteCode);
        return ResponseEntity.ok(new ApiResponse<>(partyId));
    }

}
