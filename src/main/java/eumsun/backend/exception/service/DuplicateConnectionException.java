package eumsun.backend.exception.service;

public class DuplicateConnectionException extends RuntimeException {
    public DuplicateConnectionException(String message) {
        super(message);
    }
}
