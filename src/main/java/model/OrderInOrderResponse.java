package model;
import com.google.gson.annotations.SerializedName;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInOrderResponse {
    List<Map<String,String>> ingredients;
    @SerializedName("_id")
    private String id;
    private Map<String,String> owner;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Integer price;

    public List<Map<String, String>> getIngredients() {
        return ingredients;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getOwner() {
        return owner;
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

    public Integer getPrice() {
        return price;
    }
}
