package com.Authentication.System.service;


import com.Authentication.System.dto.userRequestDto;
import com.Authentication.System.dto.userResponseDto;
import com.Authentication.System.entity.otp;
import com.Authentication.System.entity.user;
import com.Authentication.System.repository.otpRepository;
import com.Authentication.System.repository.userRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private userRepository userRepo;

    @Autowired
    private otpRepository otpRepo;

    @Autowired
    private emailService emailservice;

    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    private String registerUser(userRequestDto request){
        if(userRepo.existsByUsername(request.getUsername())){
            return "username not available or already taken!";
        }

        if(userRepo.existsBYEmail(request.getEmail())){
            return "email is already registered";
        }

        user User= new user();
        User.setUsername(request.getUsername());
        User.setEmail(request.getEmail());
        User.setPassword(passwordEncoder.encode(request.getPassword()));
        User.setEnable(false);
        userRepo.save(User);

        Random random= new Random();
        int otpGenerated= 100000+ random.nextInt(900000);
        otpRepo.findByEmail(request.getEmail()).ifPresent(otpRepo::delete);
        otp Otp= new otp();
        Otp.setEmail(request.getEmail());

        Otp.setExpiry_time(LocalDateTime.now().plusMinutes(5));
        Otp.setOtp_code(otpGenerated);
        otpRepo.save(Otp);

        emailservice.sendOtpToEmail(request.getEmail(), otpGenerated);
        return "registration successful! please check your email for otp verification.";
    }

    @Transactional
    public String verifyOtp(String email, int OtpByUser){
        otp Otp= otpRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("no otp requested for this email"));
        if(Otp.getExpiry_time().isBefore(LocalDateTime.now())){
            otpRepo.delete(Otp);
            return "otp has expired please register again";
        }
        if(Otp.getOtp_code()!= OtpByUser){
            return "Invalid otp code.";
        }

        user User= userRepo.findByEmail(email);
        User.setEnable(true);
        userRepo.save(User);
        otpRepo.delete(Otp);
        return "verification successful!... now you can able to log in";
    }

    public userResponseDto loginUser(String username, String password){
        user User= userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("invalid username or password"));

        if(!User.getEnable()){
            throw new RuntimeException("please verify you account via Otp before login.");

        }
        if(!passwordEncoder.matches(password, User.getPassword())){
            throw new RuntimeException("invalid username or password");
        }

        userResponseDto response= new userResponseDto();
        response.setEmail(User.getEmail());
        response.setUsername(User.getUsername());
        return response;
    }
}
