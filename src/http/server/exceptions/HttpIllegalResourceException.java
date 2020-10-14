package http.server.exceptions;

/**
 * Exception raised when a try to get an illegal resource is detected
 */
public class HttpIllegalResourceException extends Exception {

    public HttpIllegalResourceException() {
        super("The file is unavailable for legal reasons");
    }
}
