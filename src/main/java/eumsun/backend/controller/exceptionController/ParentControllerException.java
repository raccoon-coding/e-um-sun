package eumsun.backend.controller.exceptionController;

import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.service.DuplicateConnectionException;
import eumsun.backend.exception.service.NotMatchOffspringUser;
import eumsun.backend.exception.service.ParentNotConnectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ParentControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotMatchOffspringUser.class)
    public API<String> notOffspringUserExHandler(NotMatchOffspringUser e) {
        log.error("[NotMatchOffspringUser] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.유저_타입_에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateConnectionException.class)
    public API<String> duplicationConnectionExHandler(DuplicateConnectionException e) {
        log.error("[DuplicateConnectionException] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.중복_연결);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParentNotConnectedException.class)
    public API<String> notConnectParentExHandler(ParentNotConnectedException e) {
        log.error("[ParentNotConnectedException] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.연결_요청_에러);
    }
}
