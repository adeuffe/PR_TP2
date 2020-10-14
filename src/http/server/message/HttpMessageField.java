package http.server.message;

/**
 * The class represents the interface of header field of HTTP message (request & response)
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public interface HttpMessageField {

    /**
     * Returns the HTTP header field name
     *
     * @return the HTTP header field name
     */
    String getFieldName();
}
