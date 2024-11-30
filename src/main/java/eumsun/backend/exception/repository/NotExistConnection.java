package eumsun.backend.exception.repository;

public class NotExistConnection extends RuntimeException {
    public NotExistConnection(String message) {
        super(message);
    }
}
