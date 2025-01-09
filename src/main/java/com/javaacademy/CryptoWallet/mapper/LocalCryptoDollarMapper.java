package com.javaacademy.CryptoWallet.mapper;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
@Schema(description = "Получение курса криптовалюты")
public class LocalCryptoDollarMapper implements CryptoDollarMapper {
    @Value("${CryptoPrice}")
    private BigDecimal cryptoPrice;

    public BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency) {
        return cryptoPrice;
    }
}
