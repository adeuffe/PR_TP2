package http.server;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {

    private final HttpMethod httpMethod;
    private final String resource;
    private final String protocolVersion;
    private final HttpMessageHeader httpMessageHeader;
    private final String httpRequestBody;

    public HttpRequest(String generalHeader, List<String> requestHeader, String requestBody) throws Exception {
        Pattern pattern = Pattern.compile("([A-Z]*) (.*) (.*)");
        Matcher matcher = pattern.matcher(generalHeader);
        if (matcher.find()) {
            httpMethod = HttpMethod.valueOf(matcher.group(1));
            resource = matcher.group(2);
            protocolVersion = matcher.group(3);
            httpMessageHeader = new HttpMessageHeader(requestHeader, HttpMessageType.REQUEST);
            httpRequestBody = requestBody;
        } else {
            throw new IllegalStateException();
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getResource() {
        return resource;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public HttpMessageHeader getHttpMessageHeader() {
        return httpMessageHeader;
    }

    public String getHttpRequestBody() {
        return httpRequestBody;
    }

    @Override
    public String toString() {
        return "{"
                + "httpMethod=" + this.httpMethod + ","
                + "resource=" + this.resource + ","
                + "protocolVersion=" + this.protocolVersion + ","
                + "httpMessageHeader=" + this.httpMessageHeader + ","
                + "httpRequestBody=" + this.httpRequestBody
                + "}";
    }
}
