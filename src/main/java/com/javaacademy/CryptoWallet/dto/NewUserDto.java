package com.javaacademy.CryptoWallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {
    private String login;
    private String email;
    private String password;
}
