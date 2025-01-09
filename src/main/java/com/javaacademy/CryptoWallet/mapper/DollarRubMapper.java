package com.javaacademy.CryptoWallet.mapper;

import java.math.BigDecimal;

/**
 * Интерфейс конвертера валют
 */
public interface DollarRubMapper {
    BigDecimal dollarRubConvert(BigDecimal dollarPrice);
    BigDecimal rubDollarConvert(BigDecimal rubPrice);

}
