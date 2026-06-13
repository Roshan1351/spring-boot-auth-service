package com.Authentication.System.controller;

import com.Authentication.System.dto.LoginRequestDto;
import com.Authentication.System.dto.OtpRequestDto;
import com.Authentication.System.dto.userRequestDto;
import com.Authentication.System.dto.userResponseDto;
import com.Authentication.System.service.AuthenticationService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody userRequestDto requestdto){
        String result= authenticationService.registerUser(requestdto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequestDto request){

        String result= authenticationService.verifyOtp(request.getEmails(), request.getOtp());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto requestdto){
        try {
            return authenticationService.loginUser(requestdto.getUsername(), requestdto.getPassword());

        }catch(Exception e){
            throw new RuntimeException("login failed");
        }
    }

}
