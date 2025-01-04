package com.javaacademy.CryptoWallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoAccount {
    private final String login;
    private final CryptoCurrency currency;
    private BigDecimal count;
    private final UUID uuid;
}
