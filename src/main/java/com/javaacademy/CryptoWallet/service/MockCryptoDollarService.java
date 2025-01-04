package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
public class MockCryptoDollarService {
    @Value("${MockPrice}")
    private BigDecimal mockPrice;

    public BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency) {
        return mockPrice;
    }
}
