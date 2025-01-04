package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.repository.CryptoAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserService userService;

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
        if (userService.getByLogin(login) != null) {
            UUID uuid = UUID.randomUUID();
            CryptoAccount cryptoAccount = new CryptoAccount(login, currency, BigDecimal.ZERO, uuid);
            cryptoAccountRepository.createCryptoAccount(cryptoAccount);
            return uuid;
        }
        return null;
    }

}
