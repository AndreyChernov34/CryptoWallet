package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.mapper.CryptoDollarMapper;
import com.javaacademy.CryptoWallet.mapper.DollarRubMapper;
import com.javaacademy.CryptoWallet.repository.CryptoAccountRepository;
import com.javaacademy.CryptoWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * Сервис криптокошелька
 */
@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private static final Integer NUMBEROFDECIMAL = 8;
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserRepository userRepository;
    private final CryptoDollarMapper cryptoDollarMapper;
    private final DollarRubMapper dollarRubMapper;

    /**
     * Метод получения криптокошелька по номеру
     *
     * @param uuid номер криптокошелька
     * @return криптокошелек
     */
    public CryptoAccount getByUUIDCryptoAccount(UUID uuid) {
        if (cryptoAccountRepository.getByUUIDCryptoAccount(uuid) != null) {
            return cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        } else {
            throw new RuntimeException("кошелька с таким UUID не найдено");
        }
    }

    /**
     * Список криптокошельков у пользователя
     *
     * @param user пользователь
     * @return список криптокошельков
     */
    public List<CryptoAccount> getByLoginCryptoAccount(User user) {
        if (cryptoAccountRepository.getByLoginCryptoAccount(user.getLogin()).isEmpty()) {
            throw new RuntimeException("Кошельков у данного пользователя не найдено");
        } else {
            return cryptoAccountRepository.getByLoginCryptoAccount(user.getLogin());

        }
    }

    /**
     * Создание криптокошелька
     *
     * @param login    Логин пользователя
     * @param currency Тип криптовалюты
     * @return Номер кошелька
     */
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

    /**
     * Покупка криптовалюты криптокошелька
     *
     * @param uuid      номер криптокошелька
     * @param rubAmount сумма в рублях
     * @return строка результата операции
     */
    public String refillRubCryptoAccount(UUID uuid, BigDecimal rubAmount) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        cryptoAccount.setCount(cryptoAccount.getCount().add(rubCryptoExchange(cryptoAccount, rubAmount)));
        return "Операция прошла успешно. Куплено " + rubCryptoExchange(cryptoAccount, rubAmount) + " "
                + cryptoAccount.getCurrency();
    }

    /**
     * Продажа криптовалюты с криптокошелька
     *
     * @param uuid      номер криптокошелька
     * @param rubAmount сумма в рублях
     * @return строка результата операции
     */
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

    /**
     * Возврат суммы в рублях криптовалюты кошелька с заданным номером
     *
     * @param uuid номер кошелька
     * @return Сумма в рублях
     */
    public BigDecimal getRubBalanceCryptoAccount(UUID uuid) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.getByUUIDCryptoAccount(uuid);
        return cryptoRubExchange(cryptoAccount);
    }

    /**
     * Возврат общего баланса всех криптокошельков пользователя в рублях.
     *
     * @param login Логин пользователя
     * @return сумма в рублях
     */
    public BigDecimal getRubBalanceAllCryptoAccount(String login) {
        List<CryptoAccount> cryptoAccountList = cryptoAccountRepository.getByLoginCryptoAccount(login);
        BigDecimal rubAmount = BigDecimal.ZERO;
        for (CryptoAccount cryptoAccount : cryptoAccountList) {
            rubAmount = rubAmount.add(getRubBalanceCryptoAccount(cryptoAccount.getUuid()));
        }
        return rubAmount;
    }

    /**
     * Конвертация суммы в рублях в криптовалюту заданного кошелька, использует курс доллара и рубля.
     *
     * @param cryptoAccount криптокошелек
     * @param rubAmount     сумма  врублях
     * @return сумма в криптовалюте
     */
    private BigDecimal rubCryptoExchange(CryptoAccount cryptoAccount, BigDecimal rubAmount) {

        // тип криптовалюты на счете
        CryptoCurrency cryptoCurrency = cryptoAccount.getCurrency();

        //текущий курс криптовалюты в долларах
        BigDecimal dollarCryptoExchangeRate = cryptoDollarMapper.getDollarPrice(cryptoCurrency);

        //Cумма операции в долларах по текущему курсу
        BigDecimal dollarAmount = dollarRubMapper.rubDollarConvert(rubAmount);

        //Сумма операции в криптовалюте по текущему курсу
        BigDecimal cryptoAmount = dollarAmount.divide(dollarCryptoExchangeRate, NUMBEROFDECIMAL, RoundingMode.HALF_UP);

        return cryptoAmount;
    }

    /**
     * Конвертация криптовалюты из кошелька в рубли, использует курсы доллара и рубля.
     *
     * @param cryptoAccount криптокошелек
     * @return сумма криптовалюты в рублях по курсу
     */
    public BigDecimal cryptoRubExchange(CryptoAccount cryptoAccount) {
        // тип криптовалюты на счете
        CryptoCurrency cryptoCurrency = cryptoAccount.getCurrency();

        //текущий курс криптовалюты в долларах
        BigDecimal dollarCryptoExchangeRate = cryptoDollarMapper.getDollarPrice(cryptoCurrency);

        //Cумма операции в долларах по текущему курсу
        BigDecimal dollarAmount = cryptoAccount.getCount().multiply(dollarCryptoExchangeRate)
                .setScale(NUMBEROFDECIMAL, RoundingMode.HALF_UP);

        //Сумма операции в рублях по ткущему курсу
        BigDecimal rubAmount = dollarRubMapper.dollarRubConvert(dollarAmount);

        return rubAmount;
    }
}
