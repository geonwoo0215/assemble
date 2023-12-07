package com.geonwoo.assemble.domain.partymember.controller;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberSaveDTO;
import com.geonwoo.assemble.domain.partymember.service.PartyMemberService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PartyMemberController {

    private final PartyMemberService partyMemberService;

    @PostMapping(value = "/partyMembers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody PartyMemberSaveDTO partyMemberSaveDTO,
                    HttpServletRequest request
            ) {
        Long id = partyMemberService.save(partyMemberSaveDTO);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));

    }

    @DeleteMapping(value = "/partyMembers/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        partyMemberService.delete(id);
        return ResponseEntity.ok().build();
    }


}
