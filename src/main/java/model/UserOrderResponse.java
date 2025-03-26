package model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderResponse {
    private List<OrderInUserOrderResponse> orders;
    private boolean success;
    private Integer total;
    private Integer totalToday;
    private String message;

    public List<OrderInUserOrderResponse> getOrders() {
        return orders;
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotalToday() {
        return totalToday;
    }

    public String getMessage() {
        return message;
    }
}
