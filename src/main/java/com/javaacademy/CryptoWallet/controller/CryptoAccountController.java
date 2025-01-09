package com.javaacademy.CryptoWallet.controller;

import com.javaacademy.CryptoWallet.dto.ChangeRubCryptoAccountDto;
import com.javaacademy.CryptoWallet.dto.CreateCryptoAccountDto;
import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.service.CryptoAccountService;
import com.javaacademy.CryptoWallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с криптокошельками
 */
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
@Tag(name = "CryptoAccountController", description = "Контроллер для работы с криптокошельками")
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;
    private final UserService userService;

//    @PostConstruct
//    public void init() {
//        userService.newUser(new User("vasya", "vasya@mail.ru", "qwerty"));
//        cryptoAccountService.createCryptoAccount("vasya", CryptoCurrency.BTC);
//
//    }

    /**
     * Создание криптокошелька
     *
     * @param createCryptoAccountDto данные для создания криптокошелька
     * @return Уникальный номер кошелька
     */
    @PostMapping()
    @Operation(summary = "Создание криптокошелька")
    public ResponseEntity<UUID> createCryptoAccount(@RequestBody CreateCryptoAccountDto createCryptoAccountDto) {

        if (Arrays.stream(CryptoCurrency.values())
                .anyMatch(cryptoCurrency -> cryptoCurrency.name().equals(createCryptoAccountDto.getCryptoType()))) {
            UUID uuid = cryptoAccountService.createCryptoAccount(createCryptoAccountDto.getUsername(),
                    CryptoCurrency.valueOf(createCryptoAccountDto.getCryptoType()));
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } else {
            throw new RuntimeException("Криптовалюта задана неправильно");
        }
    }

    /**
     * Получение криптокошелька по уникальному номеру UUID
     *
     * @param id Номер криптокошелька
     * @return криптокошелек
     */
    @GetMapping("{id}")
    @Operation(summary = "Получение криптокошелька по уникальному номеру UUID")
    public ResponseEntity<CryptoAccount> getByUUIDCryptoAccount(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cryptoAccountService.getByUUIDCryptoAccount(id));
    }

    /**
     * Получение списка всех криптокошельков с заданным логином
     *
     * @param username Логин пользователя
     * @return список криптокошелька
     */
    @GetMapping()
    @Operation(summary = "Получение списка всех криптокошельков с заданным логином")
    public ResponseEntity<List<CryptoAccount>> getByLoginCryptoAccount(@RequestParam String username) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(cryptoAccountService.getByLoginCryptoAccount(userService.getByLogin(username)));
        return ResponseEntity.status(HttpStatus.OK)
                .body(cryptoAccountService.getByLoginCryptoAccount(userService.getByLogin(username)));
    }

    /**
     * Покупка криптовалюты
     *
     * @param changeRubCryptoAccountDto Данные для покупки криптовалюты
     * @return строка результата операции
     */
    @PostMapping("/refill")
    @Operation(summary = "Покупка криптовалюты")
    public ResponseEntity<String> refillRubCryptoAccount(
            @RequestBody ChangeRubCryptoAccountDto changeRubCryptoAccountDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cryptoAccountService.refillRubCryptoAccount(changeRubCryptoAccountDto.getUuid(),
                        changeRubCryptoAccountDto.getRubAmount()));
    }

    /**
     * Продажа криптовалюты
     *
     * @param changeRubCryptoAccountDto Данные для продажи криптовалюты
     * @return строка результата операции
     */
    @PostMapping("/withdrawal")
    @Operation(summary = "Продажа криптовалюты")
    public ResponseEntity<String> withdrawalRubCryptoAccount(
            @RequestBody ChangeRubCryptoAccountDto changeRubCryptoAccountDto) {
        return ResponseEntity.ok(cryptoAccountService.withdrawalRubCryptoAccount(changeRubCryptoAccountDto.getUuid(),
                changeRubCryptoAccountDto.getRubAmount()));

    }

    /**
     * Получение баланса в рублях криптокошелька по его уникальному номеру
     *
     * @param id номер криптокошелька
     * @return баланс криптокошелька в рублях
     */
    @GetMapping("/balance/{id}")
    @Operation(summary = "Получение баланса в рублях криптокошелька по его уникальному номеру")
    public ResponseEntity<BigDecimal> getBalanceById(@PathVariable UUID id) {
        return ResponseEntity.ok(cryptoAccountService.getRubBalanceCryptoAccount(id));
    }

    /**
     * Получение баланса в рублях  всех криптокошельков пользователя по логину
     *
     * @param username Логин пользователя
     * @return Сумма всех криптокошельков в рублях
     */
    @GetMapping("/balance")
    @Operation(summary = "Получение баланса в рублях  всех криптокошельков пользователя по логину")
    public ResponseEntity<BigDecimal> getBalanceByLogin(@RequestParam String username) {
        return ResponseEntity.ok(cryptoAccountService.getRubBalanceAllCryptoAccount(username));
    }
}
