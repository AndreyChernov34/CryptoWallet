package com.javaacademy.CryptoWallet.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Криптокошелек
 */
@Data
@AllArgsConstructor
@Schema(description = "Криптокошелек")
public class CryptoAccount {
    @Schema(description = "Логин пользователя")
    private final String login;
    @Schema(description = "Тип криптовалюты")
    private final CryptoCurrency currency;
    @Schema(description = "Баланс криптовалюты")
    private BigDecimal count = BigDecimal.ZERO;
    @Schema(description = "Номер криптокошелька")
    private final UUID uuid;
}
