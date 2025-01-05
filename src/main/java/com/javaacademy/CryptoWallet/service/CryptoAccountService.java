package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.mapper.CryptoDollarMapper;
import com.javaacademy.CryptoWallet.mapper.DollarRubMapper;
import com.javaacademy.CryptoWallet.repository.CryptoAccountRepository;
import com.javaacademy.CryptoWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoAccountService {
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserRepository userRepository;
    private final CryptoDollarMapper cryptoDollarMapper;
    private final DollarRubMapper dollarRubMapper;

    public CryptoAccount getByUUIDCryptoAccount(UUID uuid) {
        if (cryptoAccountRepository.getByUUIDCryptoAccount(uuid) != null) {
            return cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        } else {
            throw new RuntimeException("кошелька с таким UUID не найдено");
        }
    }

    public List<CryptoAccount> getByLoginCryptoAccount(User user) {
        if (cryptoAccountRepository.getByLoginCryptoAccount(user.getLogin()).isEmpty()) {
            throw new RuntimeException("Кошельков у данного пользователя не найдено");
        } else {
            return cryptoAccountRepository.getByLoginCryptoAccount(user.getLogin());

        }
    }

    public UUID createCryptoAccount(String login, CryptoCurrency currency) {
        if (userRepository.getByLogin(login) != null) {
            UUID uuid = UUID.randomUUID();
            CryptoAccount cryptoAccount = new CryptoAccount(login, currency, BigDecimal.ZERO, uuid);
            cryptoAccountRepository.createCryptoAccount(cryptoAccount);
            return uuid;
        } else {
            throw new RuntimeException("Пользователь с таким логином не зарегистрирован");
        }
    }

    public void refillRubCryptoAccount(UUID uuid, BigDecimal rubAmount) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        cryptoAccount.setCount(cryptoAccount.getCount().add(rubCryptoExchange(cryptoAccount, rubAmount)));
    }

    public String withdrawalRubCryptoAccount(UUID uuid, BigDecimal rubAmount) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.getByUUIDCryptoAccount(uuid);

        if (cryptoAccount.getCount().compareTo(rubCryptoExchange(cryptoAccount, rubAmount)) >= 0) {
            // снятие суммы со счета
            cryptoAccount.setCount(cryptoAccount.getCount().subtract(rubCryptoExchange(cryptoAccount, rubAmount)));
            return "Операция прошла успешно. Продано " + rubCryptoExchange(cryptoAccount, rubAmount) + " "
                    + cryptoAccount.getCurrency();

        } else {
            throw new RuntimeException("Недостаточно средств на счете");
        }
    }

    public BigDecimal getRubBalanceCryptoAccount(UUID uuid) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        return cryptoRubExchange(cryptoAccount);
    }

    public BigDecimal getRubBalanceAllCryptoAccount(String login) {
        List<CryptoAccount> cryptoAccountList = cryptoAccountRepository.getByLoginCryptoAccount(login);
        BigDecimal rubAmount = BigDecimal.ZERO;
        for (CryptoAccount cryptoAccount : cryptoAccountList) {
            rubAmount = rubAmount.add(getRubBalanceCryptoAccount(cryptoAccount.getUuid()));
        }
        return rubAmount;
    }

    private BigDecimal rubCryptoExchange(CryptoAccount cryptoAccount, BigDecimal rubAmount) {

        // тип криптовалюты на счете
        CryptoCurrency cryptoCurrency = cryptoAccount.getCurrency();

        //текущий курс криптовалюты в долларах
        BigDecimal dollarCryptoExchangeRate = cryptoDollarMapper.getDollarPrice(cryptoCurrency);

        //Cумма операции в долларах по текущему курсу
        BigDecimal dollarAmount = dollarRubMapper.rubDollarConvert(rubAmount);

        //Сумма операции в криптовалюте по текущему курсу
        BigDecimal cryptoAmount = dollarAmount.divide(dollarCryptoExchangeRate, 8, RoundingMode.HALF_UP);

        return cryptoAmount;
    }

    public BigDecimal cryptoRubExchange(CryptoAccount cryptoAccount) {
        // тип криптовалюты на счете
        CryptoCurrency cryptoCurrency = cryptoAccount.getCurrency();

        //текущий курс криптовалюты в долларах
        BigDecimal dollarCryptoExchangeRate = cryptoDollarMapper.getDollarPrice(cryptoCurrency);

        //Cумма операции в долларах по текущему курсу
        BigDecimal dollarAmount = cryptoAccount.getCount().multiply(dollarCryptoExchangeRate)
                .setScale(8, RoundingMode.HALF_UP);

        //Сумма операции в рублях по ткущему курсу
        BigDecimal rubAmount = dollarRubMapper.dollarRubConvert(dollarAmount);

        return rubAmount;
    }
}
