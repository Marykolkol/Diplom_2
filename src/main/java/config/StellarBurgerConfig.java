package config;

public class StellarBurgerConfig {
    public static final String STELLAR_BASE_URI = "https://stellarburgers.nomoreparties.site/";

    public static final String LIST_OF_INGREDIENTS_ENDPOINT = "/api/ingredients";

    public static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    public static final String LOGIN_USER_ENDPOINT = "/api/auth/login";
    public static final String LOGOUT_USER_ENDPOINT = "/api/auth/logout";
    public static final String UPDATE_TOKEN_ENDPOINT = "/api/auth/token";
    public static final String RESET_PASSWORD_ENDPOINT = "/api/password-reset";
    public static final String RESET_RESET_PASSWORD_ENDPOINT = "/api/password-reset/reset";
    public static final String USER_INFO_ENDPOINT = "/api/auth/user";

    public static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    public static final String LIST_OF_ORDERS_ENDPOINT = "/api/orders/all";
    public static final String USERS_ORDERS_ENDPOINT =  "/api/orders";
}
