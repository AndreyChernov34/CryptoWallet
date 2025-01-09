package com.javaacademy.CryptoWallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Данные для изменения суммы кошелька")
public class ChangeRubCryptoAccountDto {
    @JsonProperty("account_id")
    @Schema(description = "Уникальный номер кошелька")
    private UUID uuid;

    @JsonProperty("rubles_amount")
    @Schema(description = "Сумма в рублях")
    private BigDecimal rubAmount;
}
