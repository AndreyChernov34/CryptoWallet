package com.javaacademy.CryptoWallet.mapper;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
public class MockCryptoDollarMapper {
    @Value("${MockDollarPrice}")
    private BigDecimal mockDollarPrice;

    public BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency) {
        return mockDollarPrice;
    }
}
