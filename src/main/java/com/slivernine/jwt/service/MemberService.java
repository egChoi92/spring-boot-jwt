package com.slivernine.jwt.service;

import com.slivernine.jwt.dto.MemberDto;
import com.slivernine.jwt.entity.Authority;
import com.slivernine.jwt.entity.Member;
import com.slivernine.jwt.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.SecurityUtil;

import java.util.Collections;
import java.util.Optional;

@Service
public class MemberService {

    // UserService는 UserRepository, PasswordEncoder를 주입 받습니다.
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 로직을 수행하는 메소드
    @Transactional
    public Member signup(MemberDto memberDto) {
        // MemberDto 안에 username이 DB에 존재하면 예외 처리
        if (memberRepository.findOneWithAuthoritiesByUsername(memberDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // username이 DB에 존재하지 않으면 Authority 정보를 생성하고
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        // Member 정보를 생성해서 MemberRepository의 save 메소드를 이용해 DB에 정보를 저장
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return memberRepository.save(member);
    }

    // username에 해당하는 유저와 권한 정보를 가져온다.
    @Transactional(readOnly = true)
    public Optional<Member> getMemberWidthAuthorities(String username) {
        return memberRepository.findOneWithAuthoritiesByUsername(username);
    }

    // 현재 SecurityContext에 저장된 username에 해당하는 유저와 권한 정보만 가져온다.
    @Transactional(readOnly = true)
    public Optional<Member> getMyMemberWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername);
    }

}
