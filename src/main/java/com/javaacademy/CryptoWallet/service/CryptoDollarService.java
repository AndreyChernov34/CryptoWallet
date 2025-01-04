package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.jayway.jsonpath.JsonPath;
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
public class CryptoDollarService {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${currency.url}")
    private String url;
    @Value("${currency.header}")
    private String header;
    @Value("${currency.token}")
    private String token;


    public BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency) {
        Request request = new Request.Builder()
                .get()
                .url(url + "/simple/price?ids=" + cryptoCurrency.fullname + "&vs_currencies=usd")
                .header(header, token)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                log.info(response.toString());
                return JsonPath.parse(response.body().string())
                        .read(JsonPath.compile("$['" + cryptoCurrency.fullname + "']['usd']"),
                                BigDecimal.class);
            } else {
                throw new RuntimeException("Запрос курса доллара закончился неудачно");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
