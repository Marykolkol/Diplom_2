package model;
import java.util.Map;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private boolean success;
    private Map<String, String> user;
    private String accessToken;

    public boolean isSuccess() {
        return success;
    }

    public Map<String, String> getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getMessage() {
        return message;
    }

    private String refreshToken;
    private String message;
}
