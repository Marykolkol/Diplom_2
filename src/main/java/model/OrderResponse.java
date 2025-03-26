package model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private String name;
    private OrderInOrderResponse order;
    private boolean success;
    private String message;

    public String getName() {
        return name;
    }
    public OrderInOrderResponse getOrder() {
        return order;
    }
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
