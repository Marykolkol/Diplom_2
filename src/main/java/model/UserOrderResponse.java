package model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderResponse {
    private List<OrderInUserOrderResponse> orders;
    private boolean success;
    private Integer total;
    private Integer totalToday;
    private String message;
}
