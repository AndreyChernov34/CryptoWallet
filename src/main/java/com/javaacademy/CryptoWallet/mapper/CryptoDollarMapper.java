package com.javaacademy.CryptoWallet.mapper;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;

import java.math.BigDecimal;

public interface CryptoDollarMapper {
    BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency);
}
