package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    /**
     * Поиск пользователя по логину
     *
     * @param login Логин пользователя
     * @return пользователь
     */
    public User getByLogin(String login) {
        if (userRepository.getByLogin(login) == null) {
            log.info(login);
            throw new RuntimeException("Пользователь с таким логином не найден");
        } else {
            return userRepository.getByLogin(login);
        }
    }

    /**
     * Создание нового пользователя
     *
     * @param user Пользователь
     */
    public void newUser(User user) {
        userRepository.createUser(user);
    }

    /**
     * Изменение пароля пользователя
     * @param login логин пользователя
     * @param oldPassword   старый пароль
     * @param newPassword   новый пароль
     */
    public void changeUserPassword(String login, String oldPassword, String newPassword) {
        User currentUser = getByLogin(login);
        if (currentUser.getPassword().equals(oldPassword)) {
            currentUser.setPassword(newPassword);
            userRepository.saveUser(currentUser);
        } else {
            throw new RuntimeException("Старый пароль неверный");
        }
    }
}
