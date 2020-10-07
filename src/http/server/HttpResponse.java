package http.server;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private final HttpMessageHeader httpMessageHeader = new HttpMessageHeader();
    private List<String> httpResponseBody = new ArrayList<>();

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
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public HttpMessageHeader getHttpMessageHeader() {
        return httpMessageHeader;
    }

    public List<String> getHttpResponseBody() {
        return new ArrayList<>(httpResponseBody);
    }

    public void setHttpResponseBody(List<String> httpResponseBody) {
        this.httpResponseBody = httpResponseBody;
    }

    public void addHttpResponseBodyLine(String line) {
        httpResponseBody.add(line);
    }

    public String getStatusLine() {
        return protocolVersion + " " + statusCode + " " + reasonPhrase;
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
