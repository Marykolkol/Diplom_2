package model;

import com.google.gson.annotations.SerializedName;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInUserOrderResponse {
    List<String> ingredients;
    @SerializedName("_id")
    private String id;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Integer number;

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getNumber() {
        return number;
    }
}
