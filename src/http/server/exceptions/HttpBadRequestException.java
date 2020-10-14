package http.server.exceptions;

/**
 * Exception raised when a bad request HTTP error is detected
 *
 */
public class HttpBadRequestException extends Exception {

    public HttpBadRequestException() {
        super("The server is unable to read the request");
    }

    public HttpBadRequestException(String message) {
        super(message);
    }
}
