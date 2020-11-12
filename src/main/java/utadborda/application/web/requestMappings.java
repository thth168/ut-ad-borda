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
    
    public requestMappings() {
    }
    
}
