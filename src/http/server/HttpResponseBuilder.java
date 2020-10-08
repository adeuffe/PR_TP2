package http.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {

    public static final Map<Integer, String> STATUS_CODE = new HashMap<Integer, String>() {{
        // 1XX
        put (100, "Continue");
        put (101, "Switching Protocols");
        // 2XX
        put(200, "OK");
        put(201, "Created");
        put(202, "Accepted");
        put(203, "Non-Authoritative Information");
        put(204, "No Content");
        put(205, "Reset Content");
        put(206, "Partial Content");
        // 3XX
        put(300, "Multiple Choices");
        put(301, "Moved Permanently");
        put(302, "Found");
        put(303, "See Other");
        put(304, "Not Modified");
        put(305, "Use Proxy");
        put(307, "Temporary Redirect");
        // 4XX
        put(400, "Bad Request");
        put(401, "Unauthorized");
        put(402, "Payment Required");
        put(403, "Forbidden");
        put(404, "Not Found");
        put(405, "Method Not Allowed");
        put(406, "Not Acceptable");
        put(407, "Proxy Authentication Required");
        put(408, "Request Time-out");
        put(409, "Conflict");
        put(410, "Gone");
        put(411, "Length Required");
        put(412, "Precondition Failed");
        put(413, "Request Entity Too Large");
        put(414, "Request-URI Too Large");
        put(415, "Unsupported Media Type");
        put(416, "Requested range not satisfiable");
        put(417, "Expectation Failed");
        // 5XX
        put(500, "Internal Server Error");
        put(501, "Not Implemented");
        put(502, "Bad Gateway");
        put(503, "Service Unavailable");
        put(504, "Gateway Time-out");
        put(505, "HTTP Version not supported");
    }};

    public static void setContentType(HttpResponse httpResponse, String contentType) {
        httpResponse.getHttpMessageHeader().addField("Content-Type", contentType);
    }

    public static void setContentLength(HttpResponse httpResponse, int contentLength) {
        httpResponse.getHttpMessageHeader().addField("Content-Length", Integer.toString(contentLength));
    }

    public static void setContentLocation(HttpResponse httpResponse, String location) {
        httpResponse.getHttpMessageHeader().addField("Content-Location", location);
    }
}
