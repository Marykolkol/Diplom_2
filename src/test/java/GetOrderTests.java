import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetOrderTests {

    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private IngredientClient ingredientClient = new IngredientClient();
    private User user;
    private Order order;
    private UserResponse userResponse = new UserResponse();
    private UserOrderResponse userOrderResponse = new UserOrderResponse();

    @Before
    @DisplayName("Создание пользователя")
    public void createNewUser(){
        Map<String, String> userData = userClient.generateUserBody();
        user = new User(userData.get("email"), userData.get("password"), userData.get("name"));
        ValidatableResponse response = userClient.postCreateUser(user);
        userResponse = response.extract().body().as(UserResponse.class);

    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getOrdersWithAuthorization() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        ValidatableResponse responseIngredients = ingredientClient.getListOfIngredients();
        List<Map<String,String>> ingredients = responseIngredients.extract().path("data");
        List<String> ingredientsForOrder = ingredientClient.generateIngredientsList(ingredients);
        order = new Order(ingredientsForOrder);

        orderClient.postCreateOrder(order, userResponse.getAccessToken());

        ValidatableResponse responseUserOrders = orderClient.getUserOrders(userResponse.getAccessToken());
        userOrderResponse = responseUserOrders.extract().body().as(UserOrderResponse.class);
        responseUserOrders.assertThat().statusCode(200);

        assertTrue(userOrderResponse.isSuccess());
        assertNotNull(userOrderResponse.getTotal());
        assertNotNull(userOrderResponse.getTotalToday());
        assertNotNull(userOrderResponse.getOrders().get(0).getNumber());
        assertEquals("done",userOrderResponse.getOrders().get(0).getStatus());
        assertNotNull(userOrderResponse.getOrders().get(0).getId());

    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getOrdersWithoutAuthorization() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        ValidatableResponse responseIngredients = ingredientClient.getListOfIngredients();
        List<Map<String,String>> ingredients = responseIngredients.extract().path("data");
        List<String> ingredientsForOrder = ingredientClient.generateIngredientsList(ingredients);
        order = new Order(ingredientsForOrder);
        orderClient.postCreateOrder(order, userResponse.getAccessToken());

        ValidatableResponse responseUserOrders = orderClient.getUserOrders("");
        userOrderResponse = responseUserOrders.extract().body().as(UserOrderResponse.class);

        responseUserOrders.assertThat().statusCode(401);
        assertFalse(userOrderResponse.isSuccess());
        assertEquals("You should be authorised", userOrderResponse.getMessage());
    }

    @After
    @DisplayName("Удаление пользователя")
    public void clean() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
    }
}
