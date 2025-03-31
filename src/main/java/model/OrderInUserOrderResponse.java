package model;

import com.google.gson.annotations.SerializedName;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInUserOrderResponse {
    List<String> ingredients;
    @SerializedName("_id")
    private String id;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Integer number;
}
