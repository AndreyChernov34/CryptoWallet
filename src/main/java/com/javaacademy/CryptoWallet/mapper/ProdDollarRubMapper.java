package com.javaacademy.CryptoWallet.mapper;

import com.jayway.jsonpath.JsonPath;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.math.RoundingMode;

@Profile("prod")
@Service
@Slf4j
@RequiredArgsConstructor
@Schema(description = "Конвертор сумм в долларах и в рублях")
public class ProdDollarRubMapper implements DollarRubMapper {
    private static final Integer NUMBEROFDECIMAL = 8;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${currency.rub.url}")
    private String url;

    /**
     * Перевод суммы в долларах в рубли
     *
     * @param dollarPrice сумма в долларах
     * @return сумма в рублях
     */
    public BigDecimal dollarRubConvert(BigDecimal dollarPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return dollarPrice.divide(exchangeRate, NUMBEROFDECIMAL, RoundingMode.HALF_UP);
    }

    /**
     * Перевод суммы в рублях в доллары
     *
     * @param rubPrice Сумма в рублях
     * @return сумма в долларах
     */
    public BigDecimal rubDollarConvert(BigDecimal rubPrice) {
        BigDecimal exchangeRate = rubExchangeRate();
        return rubPrice.multiply(exchangeRate);
    }

    /**
     * Запрос курса рубля к доллару
     *
     * @return курс рубля к доллару
     */
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
