package com.geonwoo.assemble.domain.member.service;

import com.geonwoo.assemble.domain.member.dto.MemberSignUpDTO;
import com.geonwoo.assemble.domain.member.model.Member;
import com.geonwoo.assemble.domain.member.repository.MemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJdbcRepository memberJdbcRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long singUp(MemberSignUpDTO memberJoinDTO) {

        String encodePassword = encoder.encode(memberJoinDTO.getPassword());
        Member member = memberJoinDTO.toMember(encodePassword);
        Long saveId = memberJdbcRepository.save(member);
        return saveId;

    }

}
