package com.slivernine.jwt.repository;

import com.slivernine.jwt.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository를 extends하면 findAll, save 등의 메소드를 기본적으로 사용할 수 있다.
public interface MemberRepository extends JpaRepository<Member, Long> {
    // username을 기준으로 User 정보를 가져올 때 권한 정보도 함께 가져온다.
    // @EntityGraph 는 쿼리가 수행될 때 Eager 조회로 authorities 정보를 같이 가져 온다.
    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByUsername(String username);
}
