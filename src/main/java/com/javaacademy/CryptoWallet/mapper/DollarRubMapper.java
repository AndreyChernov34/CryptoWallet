package com.javaacademy.CryptoWallet.mapper;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Profile("prod")
@Service
@Slf4j
@RequiredArgsConstructor
public class DollarRubMapper {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${currency.rub.url}")
    private String url;

    public BigDecimal dollarRubConvert(BigDecimal dollarPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return dollarPrice.divide(exchangeRate, 8, RoundingMode.HALF_UP);
    }

    public BigDecimal rubDollarConvert(BigDecimal rubPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return rubPrice.multiply(exchangeRate);
    }

    private BigDecimal rubExchangeRate() {
        Request request = new Request.Builder().get().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                BigDecimal result = JsonPath.parse(response.body().string())
                        .read(JsonPath.compile("$['rates']['USD']"),
                                BigDecimal.class);
                return result;
            } else {
                throw new RuntimeException("Запрос курса рубля закончился неудачно" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
