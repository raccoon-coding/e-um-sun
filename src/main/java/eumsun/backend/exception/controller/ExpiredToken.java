package eumsun.backend.exception.controller;

public class ExpiredToken extends RuntimeException {
    public ExpiredToken(String message) {
        super(message);
    }
}
