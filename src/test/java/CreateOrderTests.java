import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class CreateOrderTests {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private IngredientClient ingredientClient = new IngredientClient();
    private User user;
    private Order order;
    private UserResponse userResponse = new UserResponse();
    private OrderResponse orderResponse = new OrderResponse();

    private static final Random RANDOM = new Random();

    @Before
    @DisplayName("Создание пользователя")
    public void createNewUser(){
        Map<String, String> userData = userClient.generateUserBody();
        user = new User(userData.get("email"), userData.get("password"), userData.get("name"));
        ValidatableResponse response = userClient.postCreateUser(user);
        userResponse = response.extract().body().as(UserResponse.class);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    public void createOrderWithAuthWithIngredients() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        ValidatableResponse responseIngredients = ingredientClient.getListOfIngredients();
        List<Map<String,String>> ingredients = responseIngredients.extract().path("data");
        List<String> ingredientsForOrder = ingredientClient.generateIngredientsList(ingredients);
        order = new Order(ingredientsForOrder);

        ValidatableResponse response = orderClient.postCreateOrder(order, userResponse.getAccessToken());
        orderResponse = response.extract().body().as(OrderResponse.class);
        response.assertThat().statusCode(200);

        assertTrue(orderResponse.isSuccess());
        assertNotNull(orderResponse.getName());
        assertNotNull(orderResponse.getOrder().getId());
        assertEquals(user.getEmail(),orderResponse.getOrder().getOwner().get("email"));
        assertEquals(user.getName(),orderResponse.getOrder().getOwner().get("name"));
        assertEquals("done",orderResponse.getOrder().getStatus());
        assertNotNull(orderResponse.getOrder().getNumber());
        assertNotNull(orderResponse.getOrder().getPrice());

        List<String> ingredientsFromOrder = new ArrayList<>();
        for (int i = 0; i < ingredientsForOrder.size(); i++) {
            ingredientsFromOrder.add(orderResponse.getOrder().getIngredients().get(i).get("_id"));
        }
        assertEquals(ingredientsForOrder, ingredientsFromOrder);
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderWithoutAuthorization() {
        ValidatableResponse responseIngredients = ingredientClient.getListOfIngredients();
        List<Map<String,String>> ingredients = responseIngredients.extract().path("data");
        List<String> ingredientsForOrder = ingredientClient.generateIngredientsList(ingredients);
        order = new Order(ingredientsForOrder);
        ValidatableResponse response = orderClient.postCreateOrder(order, "");
        orderResponse = response.extract().body().as(OrderResponse.class);
        response.assertThat().statusCode(401);
        assertFalse(orderResponse.isSuccess());
        assertEquals("You should be authorised", orderResponse.getMessage());
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    public void createOrderWithoutIngredients() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        List<String> ingredientsForOrder = new ArrayList<>();
        order = new Order(ingredientsForOrder);

        ValidatableResponse response = orderClient.postCreateOrder(order, userResponse.getAccessToken());
        orderResponse = response.extract().body().as(OrderResponse.class);
        response.assertThat().statusCode(400);
        assertFalse(orderResponse.isSuccess());
        assertEquals("Ingredient ids must be provided", orderResponse.getMessage());
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredients() {
        ValidatableResponse responseLogin = userClient.postUserLogin(user);
        userResponse = responseLogin.extract().body().as(UserResponse.class);

        List<String> ingredientsForOrder = new ArrayList<>();
        int count = RANDOM.nextInt(5)+1;
        for (int i = 0; i < count; i++) {
            ingredientsForOrder.add("test" + RANDOM.nextInt(5)+1);
        }
        order = new Order(ingredientsForOrder);

        ValidatableResponse response = orderClient.postCreateOrder(order, userResponse.getAccessToken());
        response.assertThat().statusCode(500);
    }

    @After
    @DisplayName("Удаление пользователя")
    public void clean() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
    }
}

