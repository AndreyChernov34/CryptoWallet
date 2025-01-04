package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCryptoAccountDto {
    private String username;
    @JsonProperty("crypto_type")
    private String cryptoType;
}
