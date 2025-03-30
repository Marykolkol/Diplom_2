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

public class CreateUserTests {
    private UserClient userClient = new UserClient();
    private User user;
    private UserResponse userResponse = new UserResponse();

    @Before
    public void createNewUser(){
        Map<String, String> userData = userClient.generateUserBody();
        user = new User(userData.get("email"), userData.get("password"), userData.get("name"));
    }

    @Test
    @DisplayName("Создание нового уникального пользователя")
    public void createUniqueUser() {
        ValidatableResponse response = userClient.postCreateUser(user);
        response.assertThat().statusCode(200);
        userResponse = response.extract().body().as(UserResponse.class);
        assertTrue(userResponse.isSuccess());
        assertEquals(user.getEmail(), userResponse.getUser().get("email"));
        assertEquals(user.getName(), userResponse.getUser().get("name"));
        assertNotNull(userResponse.getAccessToken());
        assertNotNull(userResponse.getRefreshToken());
    }

    @Test
    @DisplayName("Создание неуникального пользователя")
    public void createNotUniqueUser() {
        ValidatableResponse response = userClient.postCreateUser(user);
        userResponse = response.extract().body().as(UserResponse.class);

        ValidatableResponse response2 = userClient.postCreateUser(user);
        response2.assertThat().statusCode(403);
        UserResponse userResponse2 = response2.extract().body().as(UserResponse.class);
        assertFalse(userResponse2.isSuccess());
        assertEquals("User already exists", userResponse2.getMessage());

    }

    @Test
    @DisplayName("Создание пользователя с незаполненной почтой")
    public void createUserTestCheckRequiredEmail() {
        user.setEmail("");
        ValidatableResponse response = userClient.postCreateUser(user);
        response.assertThat().statusCode(403);
        userResponse = response.extract().body().as(UserResponse.class);
        assertFalse(userResponse.isSuccess());
        assertEquals("Email, password and name are required fields", userResponse.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным паролем")
    public void createUserTestCheckRequiredPassword() {
        user.setPassword("");
        ValidatableResponse response = userClient.postCreateUser(user);
        response.assertThat().statusCode(403);
        userResponse = response.extract().body().as(UserResponse.class);
        assertFalse(userResponse.isSuccess());
        assertEquals("Email, password and name are required fields", userResponse.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным именем")
    public void createUserTestCheckRequiredName() {
        user.setName("");
        ValidatableResponse response = userClient.postCreateUser(user);
        response.assertThat().statusCode(403);
        userResponse = response.extract().body().as(UserResponse.class);
        assertFalse(userResponse.isSuccess());
        assertEquals("Email, password and name are required fields", userResponse.getMessage());
    }

    @After
    @DisplayName("Удаление пользователя")
    public void clean() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
    }
}
