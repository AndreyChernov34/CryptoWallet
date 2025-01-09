package com.javaacademy.CryptoWallet;

import com.javaacademy.CryptoWallet.dto.ChangeRubCryptoAccountDto;
import com.javaacademy.CryptoWallet.dto.CreateCryptoAccountDto;
import com.javaacademy.CryptoWallet.entity.CryptoAccount;
import com.javaacademy.CryptoWallet.entity.CryptoCurrency;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.service.CryptoAccountService;
import com.javaacademy.CryptoWallet.service.UserService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private User testUser;
    private CryptoAccount testCryptoAccount;
    private UUID testUUID;

    @Value("${CryptoPrice}")
    BigDecimal testCryptoPrice;

    @Value("${DollarPrice}")
    BigDecimal testDollarPrice;

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @MockBean
    private CryptoAccountService cryptoAccountService;

    @BeforeEach
    public void setup() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/cryptowallet")
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
        testUser = new User("vasya", "vasya@mail.ru", "qwerty");
        testUUID = UUID.randomUUID();
        testCryptoAccount = new CryptoAccount("vasya", CryptoCurrency.BTC, BigDecimal.TEN, testUUID);
    }

    @Test
    @DisplayName("Создание криптокошелька")
    public void createCryptoAccountSuccess() {
        CreateCryptoAccountDto createCryptoAccountDto = new CreateCryptoAccountDto("vasya", "ETH");
        when(cryptoAccountService.createCryptoAccount("vasya", CryptoCurrency.ETH)).thenReturn(testUUID);
        UUID uuid = RestAssured
                .given(requestSpecification)
                .contentType("application/json")
                .body(createCryptoAccountDto)
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract()
                .body()
                .as(UUID.class);
        Assertions.assertEquals(testUUID, uuid);
    }

    @Test
    @DisplayName("Получение списка всех криптокошельков с заданным логином")
    public void getByLoginCryptoAccountSuccess() {
        userService.newUser(testUser);
        when(cryptoAccountService.getByLoginCryptoAccount(testUser)).thenReturn(List.of(testCryptoAccount));

        List<CryptoAccount> resultCryptoAccountList = RestAssured.given(requestSpecification)
                .contentType("application/json")
                .param("username", testUser.getLogin())
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });

        Assertions.assertEquals(1, resultCryptoAccountList.size());
        CryptoAccount resultCryptoAccount = resultCryptoAccountList.get(0);
        Assertions.assertEquals(testCryptoAccount.getLogin(), resultCryptoAccount.getLogin());
        Assertions.assertEquals(testCryptoAccount.getCurrency(), resultCryptoAccount.getCurrency());
        Assertions.assertEquals(testCryptoAccount.getCount(), resultCryptoAccount.getCount());
        Assertions.assertEquals(testCryptoAccount.getUuid(), resultCryptoAccount.getUuid());
    }

    @Test
    @DisplayName("Получение криптокошелька по уникальному номеру UUID")
    public void getByUUIDCryptoAccountAccess() {
        when(cryptoAccountService.getByUUIDCryptoAccount(testUUID)).thenReturn(testCryptoAccount);

        CryptoAccount result = RestAssured.given(requestSpecification)
                .contentType("application/json")
                .get(testUUID.toString())
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(CryptoAccount.class);


        Assertions.assertEquals(testCryptoAccount.getUuid(), result.getUuid());
        Assertions.assertEquals(testCryptoAccount.getCurrency(), result.getCurrency());
        Assertions.assertEquals(testCryptoAccount.getCount(), result.getCount());
        Assertions.assertEquals(testCryptoAccount.getLogin(), result.getLogin());
    }


    @Test
    @DisplayName("Покупка криптовалюты")
    public void refillRubCryptoAccountSuccess() {
        BigDecimal testrubAmount = BigDecimal.valueOf(1000000);
        BigDecimal testcryptoAmount = BigDecimal.TEN;

        ChangeRubCryptoAccountDto testChangeRubCryptoAccountDto =
                new ChangeRubCryptoAccountDto(testUUID, testrubAmount);

        when(cryptoAccountService.refillRubCryptoAccount(testUUID, testrubAmount))
                .thenReturn(testcryptoAmount.toString());

        String result = RestAssured.given(requestSpecification)
                .contentType("application/json")
                .body(testChangeRubCryptoAccountDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .asString();
        Assertions.assertEquals(testcryptoAmount.toString(), result);
    }

    @Test
    @DisplayName("Продажа валюты")
    public void winthdrawlRubCryptoAccountSuccess() {
        BigDecimal testcryptoAmount = BigDecimal.TEN;
        BigDecimal testrubAmount = BigDecimal.valueOf(1000000);

        ChangeRubCryptoAccountDto testChangeRubCryptoAccountDto =
                new ChangeRubCryptoAccountDto(testUUID, testrubAmount);

        when(cryptoAccountService.withdrawalRubCryptoAccount(testUUID, testrubAmount))
                .thenReturn(testrubAmount.toString());

        String result = RestAssured.given(requestSpecification)
                .contentType("application/json")
                .body(testChangeRubCryptoAccountDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Assertions.assertEquals(testrubAmount.toString(), result);
    }

    @Test
    @DisplayName("Получение баланса в рублях криптокошелька по его уникальному номеру")
    public void getBalanceByIdSuccess() {
        when(cryptoAccountService.getRubBalanceCryptoAccount(testUUID)).thenReturn(testCryptoAccount.getCount());

        BigDecimal result = RestAssured.given(requestSpecification)
                .get("balance/" + testUUID.toString())
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(BigDecimal.class);

        Assertions.assertEquals(testCryptoAccount.getCount(), result);
    }

    @Test
    @DisplayName("Получение баланса в рублях  всех криптокошельков пользователя по логину")
    public void getAllBalanceByLoginSuccess() {
        when(cryptoAccountService.getRubBalanceAllCryptoAccount(testCryptoAccount.getLogin()))
                .thenReturn(testCryptoAccount.getCount());

        BigDecimal result = RestAssured.given(requestSpecification)
                .param("username", testCryptoAccount.getLogin())
                .get("/balance")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(BigDecimal.class);
        Assertions.assertEquals(testCryptoAccount.getCount(), result);
    }
}
