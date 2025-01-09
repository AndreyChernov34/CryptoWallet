package com.javaacademy.CryptoWallet.repository;

import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserStorage userStorage;

    public User getByLogin(String login) throws RuntimeException {
        return userStorage.getByLogin(login);
    }

    public void createUser(User user) throws RuntimeException {
        userStorage.createUser(user);
    }

    public void saveUser(User user) {
        userStorage.saveUser(user);
    }

}
