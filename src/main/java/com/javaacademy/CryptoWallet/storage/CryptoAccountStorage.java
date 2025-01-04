package com.javaacademy.CryptoWallet.storage;

import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> cryptoAccountMap = new HashMap<UUID, CryptoAccount>();

    public void createCryptoAccount(CryptoAccount cryptoAccount) throws RuntimeException {
        if (cryptoAccountMap.containsKey(cryptoAccount.getUuid())) {
            throw new RuntimeException("такой UUID уже существует");
        } else {
            cryptoAccountMap.put(cryptoAccount.getUuid(), cryptoAccount);
        }
    }

    public CryptoAccount getByUUIDCryptoAccount(UUID uuid) {
        return cryptoAccountMap.get(uuid);
    }

    public List<CryptoAccount> getByLoginCryptoAccount(String login) {
        return cryptoAccountMap.values().stream().filter(cryptoAccount -> cryptoAccount.getLogin().equals(login))
                .collect(Collectors.toList());
    }
}
