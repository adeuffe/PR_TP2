package http.server;

import java.util.Arrays;

public class HttpResponse {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private final HttpMessageHeader httpMessageHeader;
    private byte[] content;

    public HttpResponse() {
        this.httpMessageHeader = new HttpMessageHeader(HttpMessageType.RESPONSE);
    }

    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.reasonPhrase = HttpResponseBuilder.STATUS_CODE.get(statusCode);
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public HttpMessageHeader getHttpMessageHeader() {
        return this.httpMessageHeader;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content, String contentType) {
        int contentLength = content.length;
        this.addHeaderField(HttpResponseHeaderField.CONTENT_TYPE, contentType);
        this.addHeaderField(HttpResponseHeaderField.CONTENT_LENGTH, Integer.toString(contentLength));
        this.content = content;
    }

    public void removeContent() {
        this.content = null;
    }

    public String getStatusLine() {
        return this.protocolVersion + " " + this.statusCode + " " + this.reasonPhrase + "\r\n";
    }

    public boolean hasBodySection() {
        return this.content != null;
    }

    public void addHeaderField(HttpMessageField httpMessageField, String value) {
        this.httpMessageHeader.addField(httpMessageField, value);
    }

    @Override
    public String toString() {
        return "{"
                + "protocolVersion=" + this.protocolVersion + ","
                + "statusCode=" + this.statusCode + ","
                + "reasonPhrase=" + this.reasonPhrase + ","
                + "httpMessageHeader=" + this.httpMessageHeader + ","
                + "content=" + new String(this.content)
                + "}";
    }
}
