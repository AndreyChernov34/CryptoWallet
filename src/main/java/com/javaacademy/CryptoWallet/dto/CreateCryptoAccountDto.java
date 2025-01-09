package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Данные для создания кошелька")
public class CreateCryptoAccountDto {
    @Schema(description = "Логин пользователя")
    private String username;
    @JsonProperty("crypto_type")
    @Schema(description = "Тип криптовалюты")
    private String cryptoType;
}
