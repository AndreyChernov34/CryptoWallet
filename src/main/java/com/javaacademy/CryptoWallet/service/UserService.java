package com.javaacademy.CryptoWallet.service;

import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public User getByLogin(String login) throws RuntimeException {
        if (userRepository.getByLogin(login) == null) {
            throw new RuntimeException("Пользователь с таким логином не найден");
        } else {
            return userRepository.getByLogin(login);
        }
    }

    public void newUser(User user) throws RuntimeException {
        userRepository.createUser(user);
    }

    public void changeUserPassword(String login, String oldPassword, String newPassword) throws RuntimeException {
        User currentUser = getByLogin(login);
        if (currentUser.getPassword().equals(oldPassword)) {
            currentUser.setPassword(newPassword);
            userRepository.saveUser(currentUser);
        } else {
            throw new RuntimeException("Пароль неверный");
        }
    }
}
