package com.geonwoo.assemble.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDTO {

    private Long id;
    private String loginId;
    private String password;
    private String email;

    @Builder
    public MemberDTO(Long id, String loginId, String password, String email) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
