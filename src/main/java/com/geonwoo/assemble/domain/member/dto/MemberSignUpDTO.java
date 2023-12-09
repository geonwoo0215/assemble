package com.geonwoo.assemble.domain.member.dto;

import com.geonwoo.assemble.domain.member.model.Member;
import lombok.Getter;

@Getter
public class MemberSignUpDTO {

    private String loginId;
    private String password;
    private String email;
    private String nickname;

    public MemberSignUpDTO(String loginId, String password, String email, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member toMember(String encodePassword) {
        return new Member(loginId, encodePassword, email, nickname);
    }
}
