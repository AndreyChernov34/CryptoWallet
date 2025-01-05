package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ChangeRubCryptoAccountDto {
    @JsonProperty("account_id")
    private UUID uuid;

    @JsonProperty("rubles_amount")
    private BigDecimal rubAmount;
}
