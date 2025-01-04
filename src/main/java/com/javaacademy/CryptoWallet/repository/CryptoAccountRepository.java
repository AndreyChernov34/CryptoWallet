package com.javaacademy.CryptoWallet.repository;

import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.storage.CryptoAccountStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CryptoAccountRepository {
    private final CryptoAccountStorage cryptoAccountStorage;

    public void createCryptoAccount(CryptoAccount cryptoAccount) {
        cryptoAccountStorage.createCryptoAccount(cryptoAccount);
    }

    public CryptoAccount getByUUIDCryptoAccount(UUID uuid) {
        return cryptoAccountStorage.getByUUIDCryptoAccount(uuid);
    }

    public List<CryptoAccount> getByLoginCryptoAccount(String login) {
        return cryptoAccountStorage.getByLoginCryptoAccount(login);
    }

}
