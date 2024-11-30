package eumsun.backend.exception.service;

public class NotMatchUserType extends RuntimeException {
    public NotMatchUserType(String message) {
        super(message);
    }
}
