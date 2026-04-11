package com.gyan.pg_management.modules.identity.api;

import com.gyan.pg_management.modules.identity.dto.request.LoginRequestDto;
import com.gyan.pg_management.modules.identity.dto.request.SignupRequestDto;
import com.gyan.pg_management.modules.identity.dto.response.LoginResponseDto;
import com.gyan.pg_management.modules.identity.dto.response.SignupResponseDto;
import com.gyan.pg_management.modules.identity.service.AuthService;
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
