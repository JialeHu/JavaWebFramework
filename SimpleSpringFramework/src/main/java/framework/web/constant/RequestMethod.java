package framework.web.constant;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private final String name;

    RequestMethod(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
