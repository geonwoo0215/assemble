package com.geonwoo.assemble.domain.member.model;

import lombok.Getter;

@Getter
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String email;

    public Member(String loginId, String password, String email) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
