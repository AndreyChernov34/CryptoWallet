package com.javaacademy.CryptoWallet.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Пользователь
 */
@Data
@AllArgsConstructor
@Schema(description = "Пользователь")
public class User {
    @Schema(description = "Логин")
    private String login;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "Пароль")
    private String password;
}
