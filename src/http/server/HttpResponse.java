package http.server;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private final HttpMessageHeader httpMessageHeader = new HttpMessageHeader();
    private String httpResponseBody;

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.reasonPhrase = HttpResponseBuilder.STATUS_CODE.get(statusCode);
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public HttpMessageHeader getHttpMessageHeader() {
        return httpMessageHeader;
    }

    public String getHttpResponseBody() {
        return httpResponseBody;
    }

    public void setHttpResponseBody(String httpResponseBody) {
        this.httpResponseBody = httpResponseBody;
    }

    public String getStatusLine() {
        return protocolVersion + " " + statusCode + " " + reasonPhrase;
    }

    public boolean hasBodySection() {
        return httpResponseBody != null;
    }

    @Override
    public String toString() {
        return "{"
                + "protocolVersion=" + protocolVersion + ","
                + "statusCode=" + statusCode + ","
                + "reasonPhrase=" + reasonPhrase + ","
                + "httpMessageHeader=" + httpMessageHeader + ","
                + "httpResponseBody=" + httpResponseBody
                + "}";
    }
}
