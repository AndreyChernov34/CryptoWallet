package com.javaacademy.CryptoWallet.controller;

import com.javaacademy.CryptoWallet.dto.NewUserDto;
import com.javaacademy.CryptoWallet.dto.UserChangePasswordDto;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public void newUser(@RequestBody NewUserDto newUserDto) throws RuntimeException {
        userService.newUser(new User(newUserDto.getLogin(), newUserDto.getEmail(), newUserDto.getPassword()));
    }

    @PostMapping("/user/reset-password")
    public void changeUserPassword(@RequestBody UserChangePasswordDto userChangePasswordDto) throws RuntimeException {
        userService.changeUserPassword(userChangePasswordDto.getLogin(), userChangePasswordDto.getOldPassword(),
                userChangePasswordDto.getNewPassword());
    }
}
