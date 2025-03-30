package model;
import java.util.Map;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private boolean success;
    private Map<String, String> user;
    private String accessToken;
    private String refreshToken;
    private String message;
}
