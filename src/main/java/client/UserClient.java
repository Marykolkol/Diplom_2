package client;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static config.StellarBurgerConfig.STELLAR_BASE_URI;
import static io.restassured.RestAssured.given;

public class UserClient {

    private static final Random RANDOM = new Random();

    public Map<String, String> generateUserBody(){
        Map<String, String> body = new HashMap<>();
        body.put("email", "email_" + RANDOM.nextInt(1000000) + "@pochta.ru");
        body.put("password", "password_" + RANDOM.nextInt(1000));
        body.put("name", "Test Testov");
        return body;
    }

    @Step("Создание пользователя")
    public ValidatableResponse postCreateUser(User user){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/register")
                        .then()
                        .log()
                        .all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse postUserLogin(User user) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/login")
                        .then()
                        .log()
                        .all();
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse postUserLogout(String refreshToken) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(Map.of("token", refreshToken))
                        .when()
                        .post("/api/auth/logout")
                        .then()
                        .log()
                        .all();
    }

    @Step("Обновление токена")
    public ValidatableResponse postUpdateToken(User user) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/token")
                        .then()
                        .log()
                        .all();
    }

    @Step("Сброс пароля")
    public ValidatableResponse postResetPassword(String email) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(Map.of("email", email))
                        .when()
                        .post("/api/password-reset")
                        .then()
                        .log()
                        .all();
    }

    @Step("Восстановление пароля")
    public ValidatableResponse postResetResetPassword(String password, String token) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(Map.of("password", password, "token", token))
                        .when()
                        .post("/api/password-reset/reset")
                        .then()
                        .log()
                        .all();
    }

    @Step("Получение информации о пользователе")
    public ValidatableResponse getUserInfo(String token){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/auth/user")
                        .then()
                        .log()
                        .all();
    }

    @Step("Обновление данных о пользователе")
    public ValidatableResponse patchUpdateUserInfo(User user, String token) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Authorization", token)
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .patch("/api/auth/user")
                        .then()
                        .log()
                        .all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Authorization", token)
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/auth/user")
                        .then()
                        .log()
                        .all();
    }

}
