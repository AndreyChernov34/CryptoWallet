package com.javaacademy.CryptoWallet.mapper;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Profile("prod")
@Service
@Slf4j
@RequiredArgsConstructor
public class DollarRubMapper {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${currency.usd.url}")
    private String url;

    public BigDecimal dollarRubConvert(BigDecimal dollarPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return dollarPrice.divide(exchangeRate);
    }

    public BigDecimal rubDollarConvert(BigDecimal rubPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return rubPrice.multiply(exchangeRate);
    }

    private BigDecimal rubExchangeRate() {
        Request request = new Request.Builder().get().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                log.info(response.toString());
                return JsonPath.parse(response.body().string())
                        .read(JsonPath.compile("$.rates.USD"),
                                BigDecimal.class);
            } else {
                throw new RuntimeException("Запрос курса рубля закончился неудачно");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
