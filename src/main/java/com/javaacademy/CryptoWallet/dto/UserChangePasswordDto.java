package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserChangePasswordDto {
        private String login;
        @JsonProperty("old_password")
        private String oldPassword;
        @JsonProperty("new_password")
        private String newPassword;
}
