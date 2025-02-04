package com.javaacademy.CryptoWallet.mapper;

import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.jayway.jsonpath.JsonPath;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Конвертер курса криптовалюты")
public class ProdCryptoDollarMapper implements CryptoDollarMapper {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${currency.usd.url}")
    private String url;
    @Value("${currency.usd.header}")
    private String header;
    @Value("${currency.usd.token}")
    private String token;

    /**
     * Получение курса криптовалюты
     *
     * @param cryptoCurrency Тип криптовалюты
     * @return курс криптовалюты в долларах
     */
    @Override
    @Schema(description = "Получение курса криптовалюты")
    public BigDecimal getDollarPrice(CryptoCurrency cryptoCurrency) {
        Request request = new Request.Builder()
                .get()
                .url(url + "/simple/price?ids=" + cryptoCurrency.fullname + "&vs_currencies=usd")
                .header(header, token)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                String jsonPath = "$['" + cryptoCurrency.fullname + "']['usd']";
                response.close();
                return JsonPath.parse(jsonString).read(jsonPath, BigDecimal.class);
            } else {
                throw new RuntimeException("Запрос курса доллара закончился неудачно");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
