package eumsun.backend.exception.controller;

public class NotEqualUserPasswordException extends RuntimeException {
    public NotEqualUserPasswordException(String message) {
        super(message);
    }
}
