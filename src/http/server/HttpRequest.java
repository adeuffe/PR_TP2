package http.server;

import java.util.List;
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
    private final String protocolVersion;
    private final HttpMessageHeader header;
    private final String content;

    /**
     * The constructor of an HTTP request
     *
     * @param requestLine the request line that correspond to the first line of the HTTP request
     * @param requestHeader the header section of the HTTP request
     * @param requestBody the body section of the HTTP request
     * @throws Exception if there's something wrong with general header or header section
     */
    public HttpRequest(String requestLine, List<String> requestHeader, String requestBody) throws Exception {
        Pattern pattern = Pattern.compile("([A-Z]*) (.*) (.*)");
        Matcher matcher = pattern.matcher(requestLine);
        if (matcher.find()) {
            httpMethod = HttpMethod.valueOf(matcher.group(1));
            resource = matcher.group(2);
            protocolVersion = matcher.group(3);
            header = new HttpMessageHeader(requestHeader, HttpMessageType.REQUEST);
            content = requestBody;
        } else {
            throw new IllegalStateException();
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
        return "{"
                + "httpMethod=" + this.httpMethod + ","
                + "resource=" + this.resource + ","
                + "protocolVersion=" + this.protocolVersion + ","
                + "header=" + this.header + ","
                + "content=" + this.content
                + "}";
    }
}
