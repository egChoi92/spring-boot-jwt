package com.slivernine.jwt.controller;

import com.slivernine.jwt.dto.LoginDto;
import com.slivernine.jwt.dto.TokenDto;
import com.slivernine.jwt.jwt.JwtFilter;
import com.slivernine.jwt.jwt.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    // 1. AuthController는 TokenProvider, AuthenticationManagerBuilder를 주입 받는다.
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    // 2. 로그인 API 경로는 "/api/authenticate" 이고 POST 요청을 받는다.
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 3. LoginDto의 username, password를 이용해서 UsernamePasswordAuthenticationToken을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 4. authenticationToken을 이용해서 authenticate 메소드가 실행될 때
        // loadUserByUsername 메소드가 실행되고 이 결과값을 가지고 Authentication 객체를 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 5. 4에서 생성한 객체를 SecurityContext에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 6. 4에서 생성한 객체를 createToken 메소드를 통해서 JWT을 생성합니다.
        String jwt = tokenProvider.createToken(authentication);

        // 7. JWT을 Response Header에도 넣어주고
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // 8. TokenDto를 이용해서 Response Body에도 넣어서 리턴
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
