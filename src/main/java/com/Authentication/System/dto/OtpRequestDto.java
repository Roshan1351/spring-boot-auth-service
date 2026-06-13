package com.Authentication.System.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OtpRequestDto {
    String emails;
    int otp;
}
