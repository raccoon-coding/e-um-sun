package eumsun.backend.exception.controller;

public class RefreshTokenRedirect extends RuntimeException {
    public RefreshTokenRedirect(String message) {
        super(message);
    }
}
