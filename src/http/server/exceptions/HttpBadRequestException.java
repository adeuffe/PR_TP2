package http.server.exceptions;

public class HttpBadRequestException extends Exception {

    public HttpBadRequestException() {
        super("The server is unable to read the request");
    }

    public HttpBadRequestException(String message) {
        super(message);
    }
}
