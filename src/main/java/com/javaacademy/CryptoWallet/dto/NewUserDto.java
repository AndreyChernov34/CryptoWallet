package com.javaacademy.CryptoWallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Данные для создания нового пользователя")
public class NewUserDto {
    @Schema(description = "Логин пользователя")
    private String login;
    @Schema(description = "Email пользователя")
    private String email;
    @Schema(description = "Пароль пользователя")
    private String password;
}
