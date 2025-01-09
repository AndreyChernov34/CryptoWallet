package com.javaacademy.CryptoWallet.controller;

import com.javaacademy.CryptoWallet.dto.NewUserDto;
import com.javaacademy.CryptoWallet.dto.UserChangePasswordDto;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Tag(name = "UserController", description = "Контроллер для работы с пользователями")
public class UserController {
    private final UserService userService;

    /**
     * Создание нового пользователя
     *
     * @param newUserDto Данные для создания пользователя
     * @return HttpStatus запроса
     */
    @PostMapping("/signup")
    @Operation(summary = "Создание нового пользователя")
    public ResponseEntity<HttpStatus> newUser(@RequestBody NewUserDto newUserDto) {
        userService.newUser(new User(newUserDto.getLogin(), newUserDto.getEmail(), newUserDto.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Смена пароля пользователя
     *
     * @param userChangePasswordDto Данные для изменения пароля
     * @return HttpStatus запроса
     */
    @PatchMapping("/reset-password")
    @Operation(summary = "Смена пароля пользователя")
    public ResponseEntity<HttpStatus> changeUserPassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {
        userService.changeUserPassword(userChangePasswordDto.getLogin(), userChangePasswordDto.getOldPassword(),
                userChangePasswordDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
