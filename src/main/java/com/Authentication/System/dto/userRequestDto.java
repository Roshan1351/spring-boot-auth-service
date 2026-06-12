package com.Authentication.System.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class userRequestDto {
    private String username;
    private String email;
    private String password;
}
