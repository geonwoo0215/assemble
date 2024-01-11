package com.geonwoo.assemble.global.auth.token.model;

import com.geonwoo.assemble.domain.member.model.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    private Long id;

    private String refreshToken;

    private Long memberId;

    private MemberRole role;

    public RefreshToken(String refreshToken, Long memberId, MemberRole role) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.role = role;
    }
}
