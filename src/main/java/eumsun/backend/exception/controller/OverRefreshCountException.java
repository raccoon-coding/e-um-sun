package eumsun.backend.exception.controller;

public class OverRefreshCountException extends RuntimeException {
    public OverRefreshCountException(String message) {
        super(message);
    }
}
