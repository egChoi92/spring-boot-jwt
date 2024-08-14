package com.slivernine.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity                 // 데이터베이스의 테이블과 1:1 매핑되는 객체
@Table(name = "`user`") // 테이블 명을 user 지정
// 롬복 어노테이션
@Getter                 // Get 관련 코드 자동 생성
@Setter                 // Set 관련 코드 자동 생성
@Builder                // Builder 관련 코드 자동 생성
@AllArgsConstructor     // Constructor 관련 코드 자동 생성
@NoArgsConstructor
public class User {

    @JsonIgnore
    @Id
    @Column(name = "`user_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    // User 객체와 권한 객체의 다대다 관계를 일대다, 다대일 관계의 조인 테이블로 정의
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authority;

}
