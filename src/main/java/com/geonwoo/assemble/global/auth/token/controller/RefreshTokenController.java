package com.geonwoo.assemble.global.auth.token.controller;

import com.geonwoo.assemble.global.auth.token.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {


    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/tokens")
    public ResponseEntity<Void> refreshAccessToken
            (
                    @CookieValue("refreshToken") String refreshToken,
                    HttpServletResponse response
            ) {
        String accessToken = refreshTokenService.reCreateAccessToken(refreshToken);
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
