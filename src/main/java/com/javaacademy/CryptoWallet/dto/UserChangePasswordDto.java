package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Данные для изменения пароля кошелька")
public class UserChangePasswordDto {
        @Schema(description = "Логин пользователя")
        private String login;
        @JsonProperty("old_password")
        @Schema(description = "Старый пароль пользователя")
        private String oldPassword;
        @JsonProperty("new_password")
        @Schema(description = "Новый пароль пользователя")
        private String newPassword;
}
