package com.geonwoo.assemble.domain.member.service;

import com.geonwoo.assemble.domain.member.dto.MemberSignUpDTO;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberJdbcRepository repository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    void signUp() {

        MemberSignUpDTO memberSignUpDTO = new MemberSignUpDTO("loginId", "password", "email");

        when(encoder.encode(memberSignUpDTO.getPassword())).thenReturn("encodePassword");
        when(repository.save(any(Member.class))).thenReturn(1L);

        memberService.singUp(memberSignUpDTO);

        verify(encoder).encode(memberSignUpDTO.getPassword());
        verify(repository).save(any(Member.class));
    }
}