import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class LoginTests {

    private UserClient userClient = new UserClient();
    private User user;
    private UserResponse userResponse = new UserResponse();

    @Before
    @DisplayName("Создание пользователя")
    public void createNewUser(){
        Map<String, String> userData = userClient.generateUserBody();
        user = new User(userData.get("email"), userData.get("password"), userData.get("name"));
        ValidatableResponse response = userClient.postCreateUser(user);
        userResponse = response.extract().body().as(UserResponse.class);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void loginUser() {
        ValidatableResponse response = userClient.postUserLogin(user);
        response.assertThat().statusCode(200);
        userResponse = response.extract().body().as(UserResponse.class);
        assertTrue(userResponse.isSuccess());
        assertEquals(user.getEmail(), userResponse.getUser().get("email"));
        assertEquals(user.getName(), userResponse.getUser().get("name"));
        assertNotNull(userResponse.getAccessToken());
        assertNotNull(userResponse.getRefreshToken());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверной почтой")
    public void loginWrongEmail() {
        user.setEmail("wrong_email@email.ru");
        ValidatableResponse response = userClient.postUserLogin(user);
        response.assertThat().statusCode(401);
        userResponse = response.extract().body().as(UserResponse.class);
        assertFalse(userResponse.isSuccess());
        assertEquals("email or password are incorrect", userResponse.getMessage());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    public void loginWrongPassword() {
        user.setPassword("wrong_password");
        ValidatableResponse response = userClient.postUserLogin(user);
        response.assertThat().statusCode(401);
        userResponse = response.extract().body().as(UserResponse.class);
        assertFalse(userResponse.isSuccess());
        assertEquals("email or password are incorrect", userResponse.getMessage());
    }

    @After
    @DisplayName("Удаление пользователя")
    public void clean() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
    }
}
