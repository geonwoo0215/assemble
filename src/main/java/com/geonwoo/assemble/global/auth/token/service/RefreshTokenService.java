package com.geonwoo.assemble.global.auth.token.service;

import com.geonwoo.assemble.domain.member.model.MemberRole;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import com.geonwoo.assemble.global.auth.token.model.RefreshToken;
import com.geonwoo.assemble.global.auth.token.repository.RefreshTokenJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenJdbcRepository refreshTokenJdbcRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public String reCreateAccessToken(String refreshToken) {

        jwtTokenProvider.validateToken(refreshToken);
        log.info("refreshToken == {}", refreshToken);
        RefreshToken token = refreshTokenJdbcRepository.findByRefreshToken(refreshToken)
                .orElseThrow(RuntimeException::new);
        String accessToken = jwtTokenProvider.createToken(token.getMemberId(), token.getRole());
        return accessToken;
    }

    @Transactional
    public void save(Long userId, MemberRole role, String refreshToken) {
        refreshTokenJdbcRepository.findByUserId(userId)
                .ifPresentOrElse(
                        token -> refreshTokenJdbcRepository.update(token.getId(), refreshToken),
                        () -> refreshTokenJdbcRepository.save(new RefreshToken(refreshToken, userId, role))
                );
    }
}
