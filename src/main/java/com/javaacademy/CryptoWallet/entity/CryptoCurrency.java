package com.javaacademy.CryptoWallet.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum CryptoCurrency {
    BTC("bitcoin"), ETH("etherium"), SOL("solana");

    public final String fullname;
}
