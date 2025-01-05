package com.javaacademy.CryptoWallet.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
@Slf4j
@RequiredArgsConstructor
public class MockDollarRubMapper extends  DollarRubMapper{
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${MockDollarPrice}")
    private BigDecimal mockDollarPrice;

    public BigDecimal dollarRubConvert(BigDecimal dollarPrice) {
        return dollarPrice.divide(mockDollarPrice);
    }

    public BigDecimal rubDollarConvert(BigDecimal rubPrice) {
        return rubPrice.multiply(mockDollarPrice);
    }
}
