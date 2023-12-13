package com.geonwoo.assemble.domain.party.controller;

import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.dto.PartySaveDTO;
import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.service.PartyService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping(value = "/partys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody PartySaveDTO partyCreateDTO,
                    @AuthenticationPrincipal Long userId,
                    HttpServletRequest request
            ) {
        Long id = partyService.save(userId, partyCreateDTO);
        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));

    }

    @GetMapping(value = "/partys/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PartyDTO>> findById
            (
                    @PathVariable("id") Long id
            ) {
        PartyDTO partyDTO = partyService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(partyDTO));
    }

    @GetMapping(value = "/partys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<PartyDTO>>> findAllByMemberId(
            @AuthenticationPrincipal Long userId
    ) {
        List<PartyDTO> list = partyService.findAllByMemberId(userId);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PatchMapping(value = "/partys/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update
            (
                    @PathVariable("id") Long id,
                    @RequestBody PartyUpdateDTO partyUpdateDTO
            ) {
        partyService.update(id, partyUpdateDTO);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping(value = "/partys/{id}")
    public ResponseEntity<Void> delete
            (
                    @PathVariable("id") Long id
            ) {
        partyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
