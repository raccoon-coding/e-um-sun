package eumsun.backend.exception.repository;

public class ExistUserData extends RuntimeException {
    public ExistUserData(String message) {
        super(message);
    }
}
