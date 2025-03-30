package client;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static config.StellarBurgerConfig.LIST_OF_INGREDIENTS_ENDPOINT;
import static config.StellarBurgerConfig.STELLAR_BASE_URI;
import static io.restassured.RestAssured.given;

public class IngredientClient {
    private static final Random RANDOM = new Random();

    @Step("Генерация списка ингредиентов")
    public List<String> generateIngredientsList(List<Map<String,String>> ingredients) {
        List<String> ingredientsForOrder = new ArrayList<>();
        int count = RANDOM.nextInt(ingredients.size()-1)+1;
        for (int i = 0; i < count; i++) {
            ingredientsForOrder.add(ingredients.get(i).get("_id"));
        }
        return ingredientsForOrder;
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getListOfIngredients(){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .when()
                        .get(LIST_OF_INGREDIENTS_ENDPOINT)
                        .then()
                        .log()
                        .all();
    }

}
