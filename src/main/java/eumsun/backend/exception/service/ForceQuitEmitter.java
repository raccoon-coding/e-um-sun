package eumsun.backend.exception.service;

public class ForceQuitEmitter extends RuntimeException {
    public ForceQuitEmitter(String message) {
        super(message);
    }
}
