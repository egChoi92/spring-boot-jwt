package com.slivernine.jwt.service;

import com.slivernine.jwt.entity.Member;
import com.slivernine.jwt.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
// 1. UserDetailsService를 implements 하고
public class CustomMemberDetailsService implements UserDetailsService {

    // 2. MemberRepository를 주입
    private final MemberRepository memberRepository;
    public CustomMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 3. loadUserByUsername 메소드를 오버라이드
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        // 3-2. 로그인 시에 DB에서 유저 정보와 권한 정보를 가져온다.
        return memberRepository.findOneWithAuthoritiesByUsername(username)
                .map(member -> createMember(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // 4. 3에서 가져온 정보를 기반으로 userdetails.User 객체를 생성하여 리턴
    private org.springframework.security.core.userdetails.User createMember(String username, Member member) {
        if (!member.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                member.getUsername(),
                member.getPassword(),
                grantedAuthorities
        );
    }

}