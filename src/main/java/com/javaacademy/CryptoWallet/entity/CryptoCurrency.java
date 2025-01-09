package com.javaacademy.CryptoWallet.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

/**
 * Виды криптовалют
 */
@RequiredArgsConstructor
@Schema(description = "Виды криптовалют")
public enum CryptoCurrency {
    BTC("bitcoin"), ETH("etherium"), SOL("solana");

    public final String fullname;
}
