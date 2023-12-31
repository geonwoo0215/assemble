package com.geonwoo.assemble.domain.partymember.controller;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTOs;
import com.geonwoo.assemble.domain.partymember.service.PartyMemberService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PartyMemberController {

    private final PartyMemberService partyMemberService;

    @PostMapping(value = "/partys/{partyId}/partyMembers")
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @PathVariable("partyId") Long partyId,
                    @AuthenticationPrincipal Long userId,
                    HttpServletRequest request
            ) {
        Long id = partyMemberService.save(userId, partyId);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));

    }

    @GetMapping(value = "/partys/{partyId}/partyMembers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PartyMemberDTOs>> findAllByPartyId(
            @PathVariable("partyId") Long partyId,
            @AuthenticationPrincipal Long userId
    ) {
        PartyMemberDTOs partyMemberDTOs = partyMemberService.findAllByPartyId(userId, partyId);
        return ResponseEntity.ok(new ApiResponse<>(partyMemberDTOs));
    }

    @DeleteMapping(value = "/partys/{partyId}/partyMembers/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        partyMemberService.delete(id);
        return ResponseEntity.ok().build();
    }


}
