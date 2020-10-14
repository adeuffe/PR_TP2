package http.server.response;

import http.server.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a controller for building an HTTP response
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class HttpResponseBuilder {

    /**
     * The list of associations between a status code and it reason phrase
     */
    public static final Map<Integer, String> STATUS_CODE = new HashMap<Integer, String>() {{
        // 1XX
        put(100, "Continue");
        put(101, "Switching Protocols");
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

    /**
     * The list of MIME types supported by this HTTP server
     */
    public static final Map<String, String> MIME = new HashMap<String, String>() {{
        put("bin", "application/octet-stream");
        put("css", "text/css");
        put("csv", "text/csv");
        put("doc", "application/msword");
        put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("eot", "application/vnd.ms-fontobject");
        put("gif", "image/gif");
        put("htm", "text/html");
        put("html", "text/html");
        put("ico", "image/x-icon");
        put("jar", "application/java-archive");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");
        put("js", "application/javascript");
        put("json", "application/json");
        put("mpeg", "video/mpeg");
        put("mp4", "video/mp4");
        put("otf", "font/otf");
        put("png", "image/png");
        put("pdf", "application/pdf");
        put("ppt", "application/vnd.ms-powerpoint");
        put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        put("rar", "application/x-rar-compressed");
        put("sh", "application/x-sh");
        put("svg", "image/svg+xml");
        put("tar", "application/x-tar");
        put("tif", "image/tiff");
        put("tiff", "image/tiff");
        put("ts", "application/typescript");
        put("ttf", "font/ttf");
        put("woff", "font/woff");
        put("woff2", "font/woff2");
        put("xhtml", "application/xhtml+xml");
        put("xls", "application/vnd.ms-excel");
        put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("xml", "application/xml");
        put("zip", "application/zip");
        put("7z", "application/x-7z-compressed");
    }};

    /**
     * Gets and returns the content type of the specified resource from it extension
     *
     * @param resource the resource URI
     * @return the content type of the specified resource
     */
    public static String getContentType(String resource) {
        String extension = ResourceManager.getFileExtension(resource);
        String contentType = null;
        if (MIME.containsKey(extension)) {
            contentType = MIME.get(extension);
        }
        return contentType;
    }
}
