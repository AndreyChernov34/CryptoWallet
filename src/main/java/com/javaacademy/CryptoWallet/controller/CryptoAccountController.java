package com.javaacademy.CryptoWallet.controller;

import com.javaacademy.CryptoWallet.dto.CreateCryptoAccountDto;
import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.service.CryptoAccountService;
import com.javaacademy.CryptoWallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;
    private final UserService userService;

    @PostMapping()
    public UUID createCryptoAccount(@RequestBody CreateCryptoAccountDto createCryptoAccountDto) {
        log.info(createCryptoAccountDto.toString());
        if (Arrays.stream(CryptoCurrency.values())
                .anyMatch(cryptoCurrency -> cryptoCurrency.name().equals(createCryptoAccountDto.getCryptoType()))) {
            return cryptoAccountService.createCryptoAccount(createCryptoAccountDto.getUsername(), CryptoCurrency
                    .valueOf(createCryptoAccountDto.getCryptoType()));
        } else {
            throw new RuntimeException("Валюта задана неправильно");
        }
    }

    @GetMapping()
    public List<CryptoAccount> getByLoginCryptoAccount(@RequestParam String username) {
        return cryptoAccountService.getByLoginCryptoAccount(userService.getByLogin(username));
    }


}
