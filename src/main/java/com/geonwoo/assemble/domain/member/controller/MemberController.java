package com.geonwoo.assemble.domain.member.controller;

import com.geonwoo.assemble.domain.member.dto.MemberDTO;
import com.geonwoo.assemble.domain.member.dto.MemberSignUpDTO;
import com.geonwoo.assemble.domain.member.service.MemberService;
import com.geonwoo.assemble.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/members",consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<Long>> singUp(@RequestBody MemberSignUpDTO memberSignUpDTO, HttpServletRequest request){
        Long id = memberService.singUp(memberSignUpDTO);
        return ResponseEntity
                .created(URI.create(request.getRequestURI()+"/"+id))
                .body(new ApiResponse<>(id));
    }
}
