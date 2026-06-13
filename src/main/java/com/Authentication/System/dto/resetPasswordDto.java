package com.Authentication.System.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class resetPasswordDto {
    private int inputOtp;
    private String password;
    private String email;

    public resetPasswordDto(int otp, String password, String email) {
        this.inputOtp= otp;
        this.password= password;
        this.email= email;
    }
}
