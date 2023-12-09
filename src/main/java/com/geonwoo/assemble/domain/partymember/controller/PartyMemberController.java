package com.geonwoo.assemble.domain.partymember.controller;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.dto.PartyMemberSaveDTO;
import com.geonwoo.assemble.domain.partymember.service.PartyMemberService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartyMemberController {

    private final PartyMemberService partyMemberService;

    @PostMapping(value = "/partys/{partyId}/partyMembers", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/partys/{partyId}/partyMembers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<PartyMemberDTO>>> findAllByPartyId(
            @PathVariable("partyId") Long partyId
    ) {
        List<PartyMemberDTO> list = partyMemberService.findAllByPartyId(partyId);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @DeleteMapping(value = "/partys/{partyId}/partyMembers/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        partyMemberService.delete(id);
        return ResponseEntity.ok().build();
    }


}
