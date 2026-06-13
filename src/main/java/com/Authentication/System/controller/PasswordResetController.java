package com.Authentication.System.controller;

import com.Authentication.System.dto.resetPasswordDto;
import com.Authentication.System.repository.otpRepository;
import com.Authentication.System.repository.userRepository;
import com.Authentication.System.service.forgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {
    @Autowired
    private userRepository userRepo;

    @Autowired
    private otpRepository otpRepo;

    @Autowired
    private forgetPasswordService forgetservice;

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestParam String email){
        try{
            String result=  forgetservice.forgetPassword(email);
            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody resetPasswordDto request){
        try{
            int otp= request.getInputOtp();
            String email= request.getEmail();
            String newPassword= request.getPassword();
            resetPasswordDto newdto= new resetPasswordDto(otp, newPassword, email);
            return forgetservice.resetPassword(newdto);

        }catch(Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
