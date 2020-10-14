package http.server.exceptions;

/**
 * Exception raised when a try to get a forbidden resource is detected
 */
public class HttpForbiddenException extends Exception {

    public HttpForbiddenException() {
        super("The targeted file is forbidden");
    }
}
