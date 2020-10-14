package http.server.response;

import http.server.message.HttpMessageField;
import http.server.message.HttpMessageHeader;
import http.server.message.HttpMessageType;

/**
 * This class represents an HTTP response
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class HttpResponse {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private final HttpMessageHeader httpMessageHeader;
    private byte[] content;

    /**
     * The constructor of an HTTP response
     */
    public HttpResponse() {
        this.httpMessageHeader = new HttpMessageHeader(HttpMessageType.RESPONSE);
    }

    /**
     * Returns the protocol version of this HTTP response
     *
     * @return the protocol version of this HTTP response
     */
    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Sets the protocol version of this HTTP response
     *
     * @param protocolVersion the new protocol version of this HTTP response
     */
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Returns the status code of this HTTP response
     *
     * @return the status code of this HTTP response
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Sets the status code of this HTTP response
     *
     * @param statusCode the new status code of this HTTP response
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.reasonPhrase = HttpResponseBuilder.STATUS_CODE.get(statusCode);
    }

    /**
     * Returns the reason phrase of this HTTP response
     *
     * @return the reason phrase of this HTTP response
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    /**
     * Returns the header of this HTTP response
     *
     * @return the header of this HTTP response
     */
    public HttpMessageHeader getHttpMessageHeader() {
        return this.httpMessageHeader;
    }

    /**
     * Returns the copy of the body content of this HTTP response
     *
     * @return the copy of the body content of this HTTP response
     */
    public byte[] getContent() {
        return this.content.clone();
    }

    /**
     * Sets the body content of this HTTP response
     *
     * @param content     the new body content of this HTTP response
     * @param contentType the content type of the specified body content
     */
    public void setContent(byte[] content, String contentType) {
        try {
            int contentLength = content.length;
            this.addHeaderField(HttpResponseHeaderField.CONTENT_TYPE, contentType);
            this.addHeaderField(HttpResponseHeaderField.CONTENT_LENGTH, Integer.toString(contentLength));
            this.content = content.clone();
        } catch (Exception e) {
            /* ignored, will never happen */
        }
    }

    /**
     * Removes the body content of this HTTP response without remove the content type (used by the HEAD method)
     */
    public void removeContentOnly() {
        this.content = null;
    }

    /**
     * Concatenates and returns the status line of this HTTP response
     *
     * @return the status line of this HTTP response
     */
    public String getStatusLine() {
        return this.protocolVersion + " " + this.statusCode + " " + this.reasonPhrase + "\r\n";
    }

    /**
     * Returns "true" if this HTTP response has a body section, "false" otherwise
     *
     * @return "true" of this HTTP response has a body section, "false" otherwise
     */
    public boolean hasBodySection() {
        return this.content != null;
    }

    /**
     * Adds the specified header field to this HTTP response
     *
     * @param httpMessageField the field to add to this HTTP response
     * @param value            the value of the field
     * @throws Exception if the specified field is valid only for HTTP requests
     */
    public void addHeaderField(HttpMessageField httpMessageField, String value) throws Exception {
        this.httpMessageHeader.addField(httpMessageField, value);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "protocolVersion=" + this.protocolVersion + ","
                + "statusCode=" + this.statusCode + ","
                + "reasonPhrase=" + this.reasonPhrase + ","
                + "httpMessageHeader=" + this.httpMessageHeader + ","
                + "content=" + new String(this.content)
                + "}";
    }
}
