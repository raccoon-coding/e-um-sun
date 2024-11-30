package eumsun.backend.exception.controller;

public class NotHaveToken extends RuntimeException {
    public NotHaveToken(String message) {
        super(message);
    }
}
