package eumsun.backend.exception.repository;

public class NotExistUserData extends RuntimeException {
    public NotExistUserData(String message) {
        super(message);
    }
}
