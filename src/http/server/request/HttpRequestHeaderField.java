package http.server.request;

import http.server.message.HttpMessageField;

/**
 * This enum represents the list of header fields for an HTTP request
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
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
    POSTMAN_TOKEN("Postman-Token"),
    PURPOSE("Purpose"),
    SEC_CH_UA("sec-ch-ua"),
    SEC_CH_UA_MOBILE("sec-ch-ua-mobile"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_USER("Sec-Fetch-User"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests");

    private final String fieldName;

    /**
     * The constructor of an instance of this enum
     *
     * @param fieldName the name of the header field
     */
    HttpRequestHeaderField(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Searches and returns the instance of this enum that correspond to the specified field name if exists
     *
     * @param fieldName the name of the targeted field
     * @return the instance of this enum that correspond to the specified field name if exists, null otherwise
     */
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
