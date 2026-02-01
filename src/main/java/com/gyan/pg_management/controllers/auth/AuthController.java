package com.gyan.pg_management.controllers.auth;

import com.gyan.pg_management.dto.request.auth.LoginRequestDto;
import com.gyan.pg_management.dto.request.auth.SignupRequestDto;
import com.gyan.pg_management.dto.response.auth.LoginResponseDto;
import com.gyan.pg_management.dto.response.auth.SignupResponseDto;
import com.gyan.pg_management.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        return ResponseEntity.ok(authService.loginByEmailAndPassword(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request){
        return ResponseEntity.ok(authService.signupByEmailAndPassword(request));
    }
}
