package eumsun.backend.controller.exceptionController;

import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.service.StringMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OffspringControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StringMismatchException.class)
    public API<String> stringMisMatch(StringMismatchException e) {
        log.error("[StringMismatchException] ex", e);
        return new API<>(APIErrorMessage.랜덤_요청_에러);
    }
}
