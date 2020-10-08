package http.server;

public enum HttpRequestHeaderField implements HttpMessageField {

    A_IM("A-IM"),
    ACCEPT("Accept"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_DATETIME("Accept-Datetime"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),
    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_MD5("Content-MD5"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    DATE("Date"),
    EXPECT("Expect"),
    FORWARDED("Forwarded"),
    FROM("From"),
    HOST("Host"),
    HTTP2_SETTINGS("HTTP2-Settings"),
    IF_MATCH("If-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),
    IF_RANGE("If-Range"),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
    MAX_FORWARDS("Max-Forwards"),
    ORIGIN("Origin"),
    PRAGMA("Pragma"),
    PROXY_AUTHORIZATION("Proxy-Authorization"),
    RANGE("Range"),
    REFERER("Referer"),
    TE("TE"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    USER_AGENT("User-Agent"),
    UPGRADE("Upgrade"),
    VIA("Via"),
    WARNING("Warning"),

    //// Non-standard fields ////
    POSTMAN_TOKEN("Postman-Token");

    private final String fieldName;

    HttpRequestHeaderField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static HttpRequestHeaderField getFieldFromName(String fieldName) {
        for (HttpRequestHeaderField httpRequestHeaderField : HttpRequestHeaderField.values()) {
            if (httpRequestHeaderField.fieldName.equalsIgnoreCase(fieldName)) {
                return httpRequestHeaderField;
            }
        }
        return null;
    }

    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{fieldName=" + this.fieldName + "}";
    }
}
