package model;
import com.google.gson.annotations.SerializedName;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
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
}
