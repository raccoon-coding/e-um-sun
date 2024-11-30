package eumsun.backend.exception.controller;

public class NotEqualOldPassword extends RuntimeException {
    public NotEqualOldPassword(String message) {
        super(message);
    }
}
