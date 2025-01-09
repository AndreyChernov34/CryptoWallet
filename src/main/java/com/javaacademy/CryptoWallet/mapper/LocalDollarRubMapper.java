package com.javaacademy.CryptoWallet.mapper;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Конвертер курса доллара")
public class LocalDollarRubMapper implements DollarRubMapper {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${DollarPrice}")
    private BigDecimal dollarPrice;

    public BigDecimal dollarRubConvert(BigDecimal dollarAmount) {
        return dollarAmount.divide(dollarPrice);
    }

    public BigDecimal rubDollarConvert(BigDecimal rubAmount) {
        return rubAmount.multiply(dollarPrice);
    }
}
