package model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private String name;
    private OrderInOrderResponse order;
    private boolean success;
    private String message;
}
