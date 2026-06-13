package com.Authentication.System.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class emailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendOtpToEmail(String email, int otp){
        try {
            SimpleMailMessage messageSend = new SimpleMailMessage();
            messageSend.setTo(email);
            messageSend.setSubject("Your registration otp verificatin code");
            messageSend.setText("welcome! verification otp code is: " + otp);

            javaMailSender.send(messageSend);
            System.out.println("EMAIL SENT SUCCESSFULLY");
        }catch(Exception e){
            e.printStackTrace();

            System.out.println(
                    "EMAIL ERROR: " + e.getMessage());
        }
    }
}
