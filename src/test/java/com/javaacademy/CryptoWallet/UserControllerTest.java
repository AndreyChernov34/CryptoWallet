package com.javaacademy.CryptoWallet;

import com.javaacademy.CryptoWallet.dto.NewUserDto;
import com.javaacademy.CryptoWallet.dto.UserChangePasswordDto;
import com.javaacademy.CryptoWallet.entity.User;
import com.javaacademy.CryptoWallet.service.UserService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
        private RequestSpecification requestSpecification;
        private ResponseSpecification responseSpecification;
    private User testUser;

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/user")
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
        testUser = new User("ivan", "ivan@mail.ru", "qwerty");
    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void newUserSuccess() {
        NewUserDto newUserDto = new NewUserDto(testUser.getLogin(), testUser.getEmail(), testUser.getPassword());

        RestAssured.given(requestSpecification)
                .contentType("application/json")
                .body(newUserDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract();

        Assertions.assertEquals(testUser, userService.getByLogin(testUser.getLogin()));
    }

    @Test
    @DirtiesContext
    @DisplayName("Изменения пароля пользователя")
    public void changeUserPasswordSuccess() {

        userService.newUser(testUser);
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(testUser.getLogin()
                , testUser.getPassword(), "newpassword");

        RestAssured.given(requestSpecification)
                .contentType("application/json")
                .body(userChangePasswordDto)
                .patch("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract();

        Assertions.assertEquals("newpassword", userService.getByLogin(testUser.getLogin()).getPassword());
    }
}
