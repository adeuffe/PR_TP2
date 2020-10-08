package http.server;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private final HttpMessageHeader httpMessageHeader;
    private String httpResponseBody;

    public HttpResponse() {
        this.httpMessageHeader = new HttpMessageHeader(HttpMessageType.RESPONSE);
    }

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

    public void setHttpResponseBody(String httpResponseBody, String contentType) {
        int contentLength = httpResponseBody.getBytes(StandardCharsets.UTF_8).length;
        this.addHeaderField(HttpResponseHeaderField.CONTENT_TYPE, contentType);
        this.addHeaderField(HttpResponseHeaderField.CONTENT_LENGTH, Integer.toString(contentLength));
        this.httpResponseBody = httpResponseBody;
    }

    public void removeHttpResponseBody() {
        this.httpResponseBody = null;
    }

    public String getStatusLine() {
        return protocolVersion + " " + statusCode + " " + reasonPhrase;
    }

    public boolean hasBodySection() {
        return httpResponseBody != null;
    }

    public void addHeaderField(HttpMessageField httpMessageField, String value) {
        this.httpMessageHeader.addField(httpMessageField, value);
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
