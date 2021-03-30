package utadborda.application.web;

public final class requestMappings {
    // ============== //
    // ADMIN MAPPINGS
    // ============== //

    public static final String ADMIN = "/admin";

    // ============== //
    // LOGIN MAPPINGS
    // ============== //

    public static final String LOGIN = "/login";
    public static final String LOGIN_SUCCESS = LOGIN + "_success";
    public static final String LOGIN_RETRY = LOGIN + "_retry";
    public static final String LOGOUT = "/logout";
    public static final String LOGOUT_SUCCESS = LOGOUT + "_success";

    // =============== //
    // SIGNUP MAPPINGS
    // =============== //

    public static final String SIGNUP = "/signup";

    // =============== //
    // ACCOUNT MAPPING
    // =============== //
    
    public static final String ACCOUNT = "/account";

    // ================= //
    // RESTAURANT MAPPING
    // ================= //

    public static final String ADD_RESTAURANT = "/addRestaurant";
    public static final String RESTAURANT = "/restaurant/{restaurant_id}";
    public static final String EDIT_RESTAURANT = "/editRestaurant/{restaurant_id}";
    public static final String MY_RESTAURANTS = "/account/restaurants";
    public static final String CLAIM_RESTAURANT = "/restaurant/claim/{restaurant_id}";

    // ================= //
    // API MAPPING
    // ================= //

    public static final String API = "/api";
    public static final String API_RESTAURANT_LIST = API + "/allRestaurants";
    public static final String API_FILTER_LIST = API + "/allFilters";

    public requestMappings() {
    }
    
}
