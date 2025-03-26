import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class ChangeUserDataTests {

    private UserClient userClient = new UserClient();
    private User user;
    private UserResponse userResponse = new UserResponse();
    private UserResponse userUpdatedResponse = new UserResponse();
    private static final Random RANDOM = new Random();

    @Before
    @DisplayName("Создание нового пользователя")
    public void createNewUser(){
        Map<String, String> userData = userClient.generateUserBody();
        user = new User(userData.get("email"), userData.get("password"), userData.get("name"));
        ValidatableResponse response = userClient.postCreateUser(user);
        userResponse = response.extract().body().as(UserResponse.class);
    }


    @Test
    @DisplayName("Смена почты для авторизованного пользователя")
    public void changeUserEmailWithAuthorization() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        user.setEmail("new_email_" + RANDOM.nextInt(100) + "@pochta.ru");
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, userResponse.getAccessToken());
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(200);
        assertTrue(userUpdatedResponse.isSuccess());
        assertEquals(user.getEmail(), userUpdatedResponse.getUser().get("email"));
        assertEquals(user.getName(), userUpdatedResponse.getUser().get("name"));
    }

    @Test
    @DisplayName("Смена имени для авторизованного пользователя")
    public void changeUserNameWithAuthorization() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        user.setName("Test Newtestov");
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, userResponse.getAccessToken());
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(200);
        assertTrue(userUpdatedResponse.isSuccess());
        assertEquals(user.getEmail(), userUpdatedResponse.getUser().get("email"));
        assertEquals(user.getName(), userUpdatedResponse.getUser().get("name"));
    }

    @Test
    @DisplayName("Смена пароля для авторизованного пользователя")
    public void changeUserPasswordWithAuthorization() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        user.setPassword("new_password_" + RANDOM.nextInt(1000));
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, userResponse.getAccessToken());
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(200);
        assertTrue(userUpdatedResponse.isSuccess());
        assertEquals(user.getEmail(), userUpdatedResponse.getUser().get("email"));
        assertEquals(user.getName(), userUpdatedResponse.getUser().get("name"));
    }

    @Test
    @DisplayName("Смена почты на существующую")
    public void changeUserEmailAlreadyExist() {
        Map<String, String> userNewData = userClient.generateUserBody();
        User newUser = new User(userNewData.get("email"), userNewData.get("password"), userNewData.get("name"));
        ValidatableResponse newResponse = userClient.postCreateUser(newUser);
        UserResponse newUserResponse = newResponse.extract().body().as(UserResponse.class);

        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        user.setEmail(newUser.getEmail());
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, userResponse.getAccessToken());
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(403);
        assertFalse(userUpdatedResponse.isSuccess());
        assertEquals("User with such email already exists", userUpdatedResponse.getMessage());

        userClient.deleteUser(newUserResponse.getAccessToken());
    }

    @Test
    @DisplayName("Смена почты для неавторизованного пользователя")
    public void changeUserEmailWithoutAuthorization() {
        user.setEmail("new_email_" + RANDOM.nextInt(100) + "@pochta.ru");
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, "");
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(401);
        assertFalse(userUpdatedResponse.isSuccess());
        assertEquals("You should be authorised", userUpdatedResponse.getMessage());
    }

    @Test
    @DisplayName("Смена пароля для неавторизованного пользователя")
    public void changeUserPasswordWithoutAuthorization() {
        user.setPassword("new_password_" + RANDOM.nextInt(1000));
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, "");
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(401);
        assertFalse(userUpdatedResponse.isSuccess());
        assertEquals("You should be authorised", userUpdatedResponse.getMessage());
    }

    @Test
    @DisplayName("Смена имени для неавторизованного пользователя")
    public void changeUserNameWithoutAuthorization() {
        user.setName("Test Newtestov");
        ValidatableResponse response = userClient.patchUpdateUserInfo(user, "");
        userUpdatedResponse = response.extract().body().as(UserResponse.class);
        response.assertThat().statusCode(401);
        assertFalse(userUpdatedResponse.isSuccess());
        assertEquals("You should be authorised", userUpdatedResponse.getMessage());
    }

    @After
    @DisplayName("Удаление пользователя")
    public void clean() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
    }
}
