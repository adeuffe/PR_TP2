package http.server.request;

import http.server.HttpMethod;
import http.server.exceptions.HttpBadRequestException;
import http.server.message.HttpMessageHeader;
import http.server.message.HttpMessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an HTTP request
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class HttpRequest {

    private final HttpMethod httpMethod;
    private final String resource;
    private String queryString = "";
    private final String protocolVersion;
    private final HttpMessageHeader header;
    private final String content;

    /**
     * The constructor of an HTTP request
     *
     * @param requestLine   the request line that correspond to the first line of the HTTP request
     * @param requestHeader the header section of the HTTP request
     * @param requestBody   the body section of the HTTP request
     * @throws HttpBadRequestException if there's something wrong with general header or header section
     */
    public HttpRequest(String requestLine, List<String> requestHeader, String requestBody) throws HttpBadRequestException {
        Pattern pattern = Pattern.compile("([A-Z]+) (.+) (.+)");
        Matcher matcher = pattern.matcher(requestLine);
        if (matcher.find()) {
            httpMethod = HttpMethod.valueOf(matcher.group(1));
            String resource = matcher.group(2);
            if (resource.equalsIgnoreCase("/")) {
                resource += "index.html";
            } else {
                Pattern pattern2 = Pattern.compile("(.+)\\?(.+)");
                Matcher matcher2 = pattern2.matcher(resource);
                if (matcher2.find()) {
                    resource = matcher2.group(1);
                    this.queryString = matcher2.group(2);
                }
            }
            this.resource = resource;
            protocolVersion = matcher.group(3);
            header = new HttpMessageHeader(requestHeader, HttpMessageType.REQUEST);
            content = requestBody;
        } else {
            throw new HttpBadRequestException("The request line has an invalid format: " + requestLine);
        }
    }

    /**
     * Returns the HTTP method of the request
     *
     * @return the HTTP method of the request
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * Returns the resource URI (relative to the resources directory of the server)
     *
     * @return the resource URI (relative to the resources directory of the server)
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns the query string of the HTTP request
     *
     * @return the query string of the HTTP request
     */
    public String getQueryString() {
        return this.queryString;
    }

    /**
     * Returns the protocol version of the request
     *
     * @return the protocol version of the request
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Returns the HTTP header section of the request
     *
     * @return the HTTP header section of the request
     */
    public HttpMessageHeader getHeader() {
        return header;
    }

    /**
     * Returns the HTTP body section of the request
     *
     * @return the HTTP body section of the request
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "httpMethod=" + this.httpMethod + ","
                + "resource=" + this.resource + ","
                + "queryString=" + this.queryString + ","
                + "protocolVersion=" + this.protocolVersion + ","
                + "header=" + this.header + ","
                + "content=" + this.content
                + "}";
    }
}
