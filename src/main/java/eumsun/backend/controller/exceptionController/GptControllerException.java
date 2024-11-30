package eumsun.backend.controller.exceptionController;

import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.service.ForceQuitEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GptControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ForceQuitEmitter.class)
    public API<String> forceQuitEmitter(ForceQuitEmitter e) {
        log.error("[ForceQuitEmitter] ex", e);
        return new API<>(APIErrorMessage.유저_강제_종료);
    }
}
