package com.Authentication.System.service;

import com.Authentication.System.dto.resetPasswordDto;
import com.Authentication.System.entity.otp;
import com.Authentication.System.entity.user;
import com.Authentication.System.repository.otpRepository;
import com.Authentication.System.repository.userRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class forgetPasswordService {
    @Autowired
    private userRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private emailService EmailService;

    @Autowired
    private otpRepository otpRepo;

    public String forgetPassword(String email){
        user Users= userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("no account found with email: "+ email));
        if(!Users.getEnable()){
            throw new RuntimeException("this account is not verified yet. please verify first.");

        }

        Random random = new Random();
        int otpGenerated= 100000+ random.nextInt(900000);
        otpRepo.findByEmail(email).ifPresent(otpRepo::delete);
        otp OtpGen= new otp();
        OtpGen.setOtp_code(otpGenerated);
        OtpGen.setEmail(email);
        OtpGen.setExpiry_time(LocalDateTime.now().plusMinutes(5));
        otpRepo.save(OtpGen);

        EmailService.sendOtpToEmail(email, otpGenerated);
        return "password reset otp send to your registered email";
    }

    public ResponseEntity<String> resetPassword(@RequestBody resetPasswordDto resetpasswordDto){
        otp OtpTable= otpRepo.findByEmail(resetpasswordDto.getEmail()).orElseThrow(()->new RuntimeException("no password reset request found for this email."));
        if(OtpTable.getExpiry_time().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body("the Otp has expired. please request a new one.");
        }

        if(OtpTable.getOtp_code()!=resetpasswordDto.getInputOtp()){
            return ResponseEntity.badRequest().body("invalid otp code!");
        }

        user userTable= userRepo.findByEmail(resetpasswordDto.getEmail()).orElseThrow(()-> new RuntimeException("user not found"));
        userTable.setPassword(passwordEncoder.encode(resetpasswordDto.getPassword()));
        userRepo.save(userTable);
        otpRepo.delete(OtpTable);
        return ResponseEntity.ok("password reset successful! you can log in with your new password");
    }
}
