package com.javaacademy.CryptoWallet.storage;

import com.javaacademy.CryptoWallet.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserStorage {
    private final Map<String, User> userMap = new HashMap<String, User>();

    public User getByLogin(String login) {
        return userMap.get(login);
    }

    public void createUser(User user) throws RuntimeException {
        if (userMap.containsKey(user.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже зарегистрирован");
        } else {
            saveUser(user);
        }
    }

    public void saveUser(User user) {
        userMap.put(user.getLogin(), user);

    }
}
