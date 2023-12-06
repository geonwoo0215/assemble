package com.geonwoo.assemble.domain.party.controller;

import com.geonwoo.assemble.domain.party.dto.PartyCreateDTO;
import com.geonwoo.assemble.domain.party.dto.PartyDTO;
import com.geonwoo.assemble.domain.party.service.PartyService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping(value = "/partys", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody PartyCreateDTO partyCreateDTO,
                    HttpServletRequest request
            ) {
        Long id = partyService.save(partyCreateDTO);
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
}
