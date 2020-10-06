package http.server;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {

    private final HttpMethod httpMethod;
    private final String resource;
    private final String protocol;
    private final HttpRequestHeader httpRequestHeader;
    private final List<String> httpRequestBody;

    public HttpRequest(String generalHeader, List<String> requestHeader, List<String> requestBody) {
        Pattern pattern = Pattern.compile("([A-Z]*) (.*) (.*)");
        Matcher matcher = pattern.matcher(generalHeader);
        if (matcher.find()) {
            httpMethod = HttpMethod.valueOf(matcher.group(1));
            resource = matcher.group(2);
            protocol = matcher.group(3);
            httpRequestHeader = new HttpRequestHeader(requestHeader);
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

    public String getProtocol() {
        return protocol;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public List<String> getHttpRequestBody() {
        return new ArrayList<>(httpRequestBody);
    }

    @Override
    public String toString() {
        return "{"
                + "httpMethod=" + this.httpMethod + ","
                + "resource=" + this.resource + ","
                + "protocol=" + this.protocol + ","
                + "httpRequestHeader=" + this.httpRequestHeader
                + "}";
    }
}
