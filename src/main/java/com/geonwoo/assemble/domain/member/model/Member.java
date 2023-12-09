package com.geonwoo.assemble.domain.member.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private MemberRole role;

    public Member(String loginId, String password, String email, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = MemberRole.USER;
    }
}
