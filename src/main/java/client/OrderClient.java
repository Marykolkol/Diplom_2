package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

import static config.StellarBurgerConfig.STELLAR_BASE_URI;
import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public ValidatableResponse postCreateOrder(Order order, String token){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Authorization", token)
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("/api/orders")
                        .then()
                        .log()
                        .all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getListOfOrders(){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/orders/all")
                        .then()
                        .log()
                        .all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getUserOrders(String token){
        return
                given()
                        .log()
                        .all()
                        .baseUri(STELLAR_BASE_URI)
                        .header("Authorization", token)
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/orders")
                        .then()
                        .log()
                        .all();
    }
}
